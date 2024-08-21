from fastapi import FastAPI
from uvicorn import run

from entity.raft import RaftDto
from loopMain import add, delete

app = FastAPI()


# 定义路由
@app.post("/api/python/raft/add")
async def addRaft(raft: RaftDto):

    add(raft)

    return {
        "code": 200,
        "msg": "ok",
        "data": None
    }


@app.post("/api/python/raft/delete")
async def deleteRaft(raftId: int):

    delete(raftId)

    return {
        "code": 200,
        "msg": "ok",
        "data": None
    }


if __name__ == "__main__":

    run(app, host="127.0.0.1", port=9008)
