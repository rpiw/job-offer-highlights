from uuid import UUID

from fastapi import FastAPI
from keybert import KeyBERT
from pydantic import BaseModel, Field

app: FastAPI = FastAPI()


class JobOfferExtract(BaseModel):
    id: UUID = Field(gt=0, description="UUID")
    content: str = Field(min_length=30, max_length=30000,
                         description="A job offer to extract a keywords from.")


@app.post("/extract")
async def extract_keywords(job_offer_extract: JobOfferExtract):
    return bert_extraction(job_offer_extract.content)


def bert_extraction(text: str) ->  list[tuple[str, float]] | list[list[tuple[str, float]]]:
    kw_model = KeyBERT()
    return kw_model.extract_keywords(text)
