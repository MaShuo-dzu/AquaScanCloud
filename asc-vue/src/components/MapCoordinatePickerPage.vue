<template>
  <div>
    <div class="input-card">
      <div class="input-row">
        <h4>左击获取：</h4>
        <input type="text" readonly id="lnglat">
      </div>
      <div class="input-item">
        <input type="button" id="submit-raft" value="创建新渔排点" v-show="lnglat" >
      </div>
    </div>

    <br>
    <div id="container" class="map"></div>
  </div>
</template>
  
<script>
import AMapLoader from "@amap/amap-jsapi-loader";
window._AMapSecurityConfig = {
  // 安全密钥
  securityJsCode: "f7bcf08ea0ab8f5ba5053af0dc96b40d",
};

export default {
  name: "MapCoordinatePickerPage",
  data() {
    return {
      lnglat: "",
      // 地图实例
      map: null,
      // 标记实例
      marker: null
    };
  },
  mounted() {
    this.initMap()
  },
  methods: {
    addMarker(lnglat) {
      // 如果已有标记，则移除
      if (this.marker) {
        this.marker.setMap(null);
      }

      // 创建新标记
      this.marker = new AMap.Marker({
        position: lnglat,
        map: this.map
      });

      // 将新标记添加到地图
      this.map.add(this.marker);
    },
    initMap() {
      AMapLoader.load({
        // 你申请的Key
        key: "6b75947a34d623fd783ced7ba44c9d7a",
        version: "2.0",
        // 需要用到的插件
        plugins: ["AMap.Geocoder", "AMap.AutoComplete"],
      })
        .then((AMap) => {
          this.map = new AMap.Map("container", {
            viewMode: "3D", //是否为3D地图模式
            zoom: 5, //初始化地图级别
            center: [105.602725, 37.076636], //初始化地图中心点位置
            resizeEnable: true
          });
          //点击获取经纬度
          this.map.on("click", (e) => {
            document.getElementById("lnglat").value = e.lnglat.getLng() + ',' + e.lnglat.getLat();
            this.lnglat = e.lnglat.getLng() + ',' + e.lnglat.getLat();
            this.addMarker(e.lnglat);
          });
        })
        .catch((err) => {
          // 错误
          console.log(err);
        });
    },
  }
};
</script>
  
<style scoped>
.input-card {
  position: absolute;
  top: 85%;
  left: 79%;
  padding: 15px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 300px;
  z-index: 3;
}

.input-row {
  display: flex;
  align-items: center;
  justify-content: space-between; /* 让内容均匀分布 */
  margin-bottom: 10px;
}

.input-row h4 {
  margin: 0;
  font-size: 14px;
  color: #333;
  flex: 0 0 26%; /* 标题占45%的宽度 */
}

.input-row input {
  width: 74%; /* 输入框占55%的宽度 */
  padding: 6px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.input-item {
  display: flex;
  justify-content: center; /* 居中提交按钮 */
}

.input-item input[type="button"] {
  padding: 8px 16px;
  font-size: 14px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.input-item input[type="button"]:hover {
  background-color: #0056b3;
}

.map {
  border-radius: 10px; /* 圆角 */
  border: 2px solid #ccc; /* 边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 阴影 */
  background-color: rgba(255, 255, 255); /* 背景色 + 透明度 */
  padding: 0px;
  margin: 0px;
  height: 680px;
  width: 100%;
}
</style>
