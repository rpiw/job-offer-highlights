FROM ghcr.io/astral-sh/uv:python3.13-bookworm-slim

WORKDIR /app

COPY pyproject.toml ./
RUN uv sync  #  it will fetch pytorch and scikit-learn, the largest dependencies

COPY main.py README.md pyproject.toml ./
RUN uv sync --compile-bytecode
CMD uv run main.py