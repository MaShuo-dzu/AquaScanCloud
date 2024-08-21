import threading
from io import BytesIO

import requests
from PIL import Image

from entity.raft import RaftDto, SegVo
from httpUtil import send_create_fish_raft_monitor_request, get_all_fish_rafts
from predict import run
from safeList import ThreadSafeList


q = ThreadSafeList()


def init(list):
    """

    :param list: RaftDto list
    :return:
    """
    for each_dto in list:
        q.append(each_dto)



def add(item: RaftDto):
    q.append(item)



def delete(id: int):
    for i in range(len(q)):
        if q[i].id == id:
            q.remove(q[i])


def getmap(longitude, latitude):

    api_url = "https://restapi.amap.com/v3/staticmap"


    params = {
        "location": "" + longitude + "," + latitude,
        "zoom": "10",
        "size": "1024*1024",
        "layers": "1",
        "key": "3aa8f06ff8eaea6236d07099d50b7428"
    }


    response = requests.get(api_url, params=params)


    if response.status_code == 200:

        image = Image.open(BytesIO(response.content))


        image.save("satellite_image.png")
        print("图像已保存为'satellite_image.png'")
    else:
        print(f"请求失败，状态码：{response.status_code}")


# 监测循环
def loopMain():
    for raft in q:

        temp=getmap(raft.longitude,raft.latitude)

        if temp is not None:
            temp=np.dot(temp,raft.mask)

            run.predict("./temp", "./output")

            send_create_fish_raft_monitor_request(SegVo(raft.id, 1, 1))

            threading.Timer(60, loopMain).start()
        else:
            print("遥感信息获取失败")
            break


if __name__ == '__main__':

    init(get_all_fish_rafts())

    threading.Timer(60, loopMain).start()
