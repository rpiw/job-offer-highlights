FROM ghcr.io/astral-sh/uv:python3.13-bookworm-slim

ENV USER=user \
    PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1 \
    UV_PROJECT_ENVIRONMENT=/usr/local

ENV APP_DIR=/app
WORKDIR $APP_DIR

RUN apt-get update && apt-get install --no-install-recommends -y curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    && useradd -m -s /bin/bash $USER

RUN --mount=type=cache,target=$APP_DIR/.cache/uv \
    --mount=type=bind,source=uv.lock,target=uv.lock \
    --mount=type=bind,source=pyproject.toml,target=pyproject.toml \
    uv sync --frozen --no-install-project

COPY main.py $APP_DIR

EXPOSE 8080

USER $USER
CMD ["uvicorn", "main:app", "--host",  "0.0.0.0", "--port", "8080"]