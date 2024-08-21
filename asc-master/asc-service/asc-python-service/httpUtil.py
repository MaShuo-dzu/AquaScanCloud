import requests

from entity.raft import SegVo


def get_all_fish_rafts(url="http://localhost:9003/fish-raft-point"):
    response = requests.get(url)
    if response.status_code == 200:
        print("所有渔排点信息获取成功:", response.json()["data"])
        return response.json()["data"]
    else:
        print(f"获取渔排点信息失败: {response.json()}")
        return None


def send_create_fish_raft_monitor_request(add_raft_monitor_data, url="http://localhost:9004/fish-raft-monitor"):
    json_data = add_raft_monitor_data.to_json()

    headers = {'Content-Type': 'application/json'}
    response = requests.post(url, data=json_data, headers=headers)


    if response.status_code == 200:
        print("请求成功")

        print(response.json())
    else:
        print(f"请求失败，状态码：{response.status_code}")


# 使用示例
if __name__ == "__main__":

    add_raft_monitor = SegVo(
        id=1,
        raftNumber=2,
        raftArea=3
    )


    get_all_fish_rafts()
