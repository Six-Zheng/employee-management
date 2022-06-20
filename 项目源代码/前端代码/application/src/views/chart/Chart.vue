<template>
  <div>
    <div style="display: flex; align-items: center; justify-content: left">
      <h1 style="padding-left: 30px; font-size: 15px">企业总人数: {{counts.count}}</h1>
      <el-tag size="medium" effect="dark" type="primargy;" style="margin: 25px; margin-right: 10px;">男</el-tag>{{counts.maleCount}}
      <el-tag size="medium" effect="dark" type="danger" style="margin-left: 25px; margin-right: 10px;">女</el-tag>{{counts.femaleCount}}
    </div>
    <div style="text-align: center; font-size: 40px;padding-bottom: 20px">部门人数占比图</div>
    <div id="department" style="width: 100%; height: 700px">
    </div>
    <div style="text-align: center; font-size: 40px;padding-bottom: 20px">岗位人数占比图</div>
    <div id="post"  style="width: 100%; height: 800px">
    </div>
  </div>
</template>

<script>
import dataService from '@/api/dataService'
import * as echarts from 'echarts';

export default {
  name: 'Chart',
  mounted () {
    this.getData()
  },
  computed: {

  },
  data() {
    return {
      counts: '',
      departmentCounts: '',
      postCounts: '',
      departmentDom: document.getElementById('department'),
      postDom: document.getElementById('post')
    }
  },
  methods: {
    async getData() {
      await dataService.getData().then(res => {
        console.log(res.data)
        this.counts = res.data.counts
        this.departmentCounts = res.data.departmentCounts
        this.postCounts = res.data.postCounts

        let departmentData = []
        let postData = []

        for (let i = 0; i <= this.departmentCounts.departmentName.length; i++) {
          departmentData.push({
            name: this.departmentCounts.departmentName[i],
            value: this.departmentCounts.departmentValue[i]
          })
        }

        for (let i = 0; i <= this.postCounts.postName.length; i++) {
          postData.push({
            name: this.postCounts.postName[i],
            value: this.postCounts.postValue[i]
          })
        }

        let departmentOption = {
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '3%',
            bottom: '5%',
            left: 'center',
            textStyle: {
              fontSize: '30px',
            }
          },
          series: [
            {
              name: 'Access From',
              type: 'pie',
              radius: ['70%', '50%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 6
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '30',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: departmentData
            }
          ]
        };
        var departmentDom = document.getElementById('department');
        var departmentChart = echarts.init(departmentDom);
       departmentOption && departmentChart.setOption(departmentOption)

        const postOption = {
          xAxis: {
            type: 'category',
            data: this.postCounts.postName,
            axisLabel:{
              // formatter:function(value){
              //   return value.split("").join("\n");
              // }
            },
          },
          tooltip: {
            show: true
          },
            yAxis: {
            type: 'value'
          },
          series: [
            {
              data: this.postCounts.postValue,
              type: 'bar',
              itemStyle: {
                normal: {
                  //这里是重点
                  color: function(params) {
                    //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                    var colorList = ['#c23531','#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83', '#ca8622', '#FFEFD5'];

                    if (params.dataIndex >= colorList.length) {
                      const index = params.dataIndex - colorList.length;
                      return colorList[index]
                    }

                    return colorList[params.dataIndex]
                  }
                }
              }
            }
          ]
        };

        var postDom = document.getElementById('post');
        var postChart = echarts.init(postDom);
        postOption && postChart.setOption(postOption)
      })
    }
  }
}
</script>

<style scoped>

</style>
