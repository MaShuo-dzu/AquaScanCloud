import json


class RaftDto:
    def __init__(self, id=None, fishermen_id=None, longitude=None, latitude=None, name=None, zoom=None, size=None,
                 message=None, mask=None):
        """

        :param id: 对应渔排点数据表id
        :param fishermen_id: 对应渔民数据表id
        :param longitude: 经度
        :param latitude: 纬度
        :param name: 渔排点备注
        :param zoom: 地图级别
        :param size: 地图大小
        :param message: 地图说明
        :param mask: 由用户创建的多边形渔排掩码图
        """
        self.id = id
        self.fishermen_id = fishermen_id
        self.longitude = longitude  # 经度
        self.latitude = latitude  # 纬度
        self.name = name
        self.zoom = zoom
        self.size = size
        self.message = message
        self.mask = mask


class SegVo:
    def __init__(self, id=None, raftNumber=None, raftArea=None):
        """
        
        :param id: 渔排点id
        :param raftNumber: 渔排点渔排数量
        :param raftArea: 渔排点面积
        """
        self.raftArea = raftArea
        self.id = id
        self.raftNumber = raftNumber

    def to_json(self):
        return json.dumps(self.__dict__, default=str)
