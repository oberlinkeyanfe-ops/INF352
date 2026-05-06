from pathlib import Path
import re
import textwrap


def escape_pdf_text(text: str) -> str:
    return text.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)")


def markdown_to_lines(md: str) -> list[str]:
    lines: list[str] = []
    in_code = False
    for raw in md.splitlines():
        line = raw.rstrip()
        if line.startswith("```"):
            in_code = not in_code
            continue
        if in_code:
            lines.append(f"    {line}")
            continue
        if line.startswith("#"):
            text = re.sub(r"^#+\\s*", "", line).strip()
            lines.append(text.upper())
            continue
        if line.startswith("- "):
            lines.append(f"- {line[2:].strip()}")
            continue
        lines.append(line)
    return lines


def wrap_lines(lines: list[str], width: int = 95) -> list[str]:
    wrapped: list[str] = []
    for line in lines:
        if not line:
            wrapped.append("")
            continue
        wrapped.extend(textwrap.wrap(line, width=width, break_long_words=False, break_on_hyphens=False))
    return wrapped


def paginate(lines: list[str], lines_per_page: int = 42) -> list[list[str]]:
    pages: list[list[str]] = []
    current: list[str] = []
    for line in lines:
        current.append(line)
        if len(current) >= lines_per_page:
            pages.append(current)
            current = []
    if current:
        pages.append(current)
    return pages


def make_content_stream(page_lines: list[str]) -> bytes:
    commands = ["BT", "/F1 11 Tf", "45 790 Td", "14 TL"]
    for i, line in enumerate(page_lines):
        prefix = "" if i == 0 else "T* "
        commands.append(f"{prefix}({escape_pdf_text(line)}) Tj")
    commands.append("ET")
    return "\n".join(commands).encode("latin-1", errors="replace")


def build_pdf(pages: list[list[str]]) -> bytes:
    objects: list[bytes] = []

    def add_object(data: bytes) -> int:
        objects.append(data)
        return len(objects)

    font_id = add_object(b"<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>")
    page_ids: list[int] = []

    for page_lines in pages:
        stream = make_content_stream(page_lines)
        content_id = add_object(
            f"<< /Length {len(stream)} >>\nstream\n".encode("latin-1")
            + stream
            + b"\nendstream"
        )
        page_id = add_object(
            (
                "<< /Type /Page /Parent {pages} 0 R /MediaBox [0 0 595 842] "
                f"/Resources << /Font << /F1 {font_id} 0 R >> >> "
                f"/Contents {content_id} 0 R >>"
            ).encode("latin-1")
        )
        page_ids.append(page_id)

    kids = " ".join(f"{pid} 0 R" for pid in page_ids)
    pages_id = add_object(f"<< /Type /Pages /Count {len(page_ids)} /Kids [{kids}] >>".encode("latin-1"))
    catalog_id = add_object(f"<< /Type /Catalog /Pages {pages_id} 0 R >>".encode("latin-1"))

    pdf = bytearray(b"%PDF-1.4\n%\xe2\xe3\xcf\xd3\n")
    offsets = [0]
    for i, obj in enumerate(objects, start=1):
        offsets.append(len(pdf))
        if i in page_ids:
            obj = obj.replace(b"{pages}", str(pages_id).encode("latin-1"))
        pdf.extend(f"{i} 0 obj\n".encode("latin-1"))
        pdf.extend(obj)
        pdf.extend(b"\nendobj\n")

    xref_start = len(pdf)
    pdf.extend(f"xref\n0 {len(objects) + 1}\n".encode("latin-1"))
    pdf.extend(b"0000000000 65535 f \n")
    for offset in offsets[1:]:
        pdf.extend(f"{offset:010d} 00000 n \n".encode("latin-1"))
    pdf.extend(
        (
            f"trailer\n<< /Size {len(objects) + 1} /Root {catalog_id} 0 R >>\n"
            f"startxref\n{xref_start}\n%%EOF\n"
        ).encode("latin-1")
    )
    return bytes(pdf)


def main() -> None:
    root = Path(__file__).resolve().parent.parent
    md_path = root / "docs" / "test-report.md"
    pdf_path = root / "docs" / "test-report.pdf"

    if not md_path.exists():
        raise FileNotFoundError(f"Missing markdown report: {md_path}")

    md = md_path.read_text(encoding="utf-8")
    lines = markdown_to_lines(md)
    wrapped = wrap_lines(lines)
    pages = paginate(wrapped)
    pdf_path.write_bytes(build_pdf(pages))
    print(pdf_path)


if __name__ == "__main__":
    main()