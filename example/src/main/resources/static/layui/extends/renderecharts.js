layui.define(['jquery'], function(exports) {
	var $ = layui.jquery;

	var renderecharts = {};
	var dataAll = null

	renderecharts.bar3d = function (data,dataType){
		var chartsData = renderecharts.handleData(data,dataType);
		dataAll = chartsData;
		if(dataType=='single'){
			var seriesList = [];
			chartsData.dataList[0].list.forEach(function (cur,idx,arr){
				var a = [chartsData.originData.xAxisOrigin[idx],0,cur];
				seriesList.push(a);
			})
		}else if(dataType=='multi'){
			var seriesList = [];
			chartsData.dataList.forEach(function (cur,idx,arr){
				cur.list.forEach(function (cur1,idx1,arr1){

				})
				var a = [chartsData.originData.xAxisOrigin[idx],0,cur];
				seriesList.push(a);
			})
		}

		var myChart=echarts.init(document.getElementById(module+'charts'));
		var option = {
			xAxis3D: {
				type: 'category',
				nameGap: 100,
				name: chartsData.originData.xLabel,
				data: chartsData.originData.xAxisOrigin,
				axisLabel: {
					interval: 0,
					margin: 10,
					textStyle: {
						fontSize: 14,
						color : '#1892ff'
					}
				},
				axisLine:{
					lineStyle:{
						color:'#1892ff',
						width:2
					}
				}
			},
			yAxis3D: {
				type: 'category',
				nameGap: 1,
				data: []
			},
			zAxis3D: {
				name: '数量',
				type: 'value',
				minInterval: 1,
				nameGap: 100,
				axisLabel: {
					interval: 0,
					margin: 10,
					textStyle: {
						fontSize: 14,
						color : '#1892ff'
					}
				},
				axisLine:{
					lineStyle:{
						color:'#1892ff',
						width:2
					}
				}
			},
			grid3D: {
				boxWidth: 400,
				boxDepth: 20,
				light: {
					main: {
						intensity: 1.2
					},
					ambient: {
						intensity: 0.3
					}
				},
				viewControl: {
					rotateSensitivity: 0,  // 不能旋转
					zoomSensitivity: 0, // 不能缩放
					alpha: 0, //控制场景平移旋转
					beta: 0
				}
			},
			series: [{
				type: 'bar3D',
				data: seriesList,
				shading: 'color',
				barSize:14,
				label: {
					show: false,
					fontSize: 16,
					borderWidth: 1
				},
				itemStyle: {
					color: '#abd9e9'
				},
				emphasis: {
					label: {
						fontSize: 20,
						color: '#313695',
						textStyle: {
							color: '#1892ff'
						}
					},
					itemStyle: {
						color: '#313695'
					}
				}
			}]
		}
		myChart.setOption(option,true);
	}

	renderecharts.bar2d = function (data,dataType) {
		var chartsData = renderecharts.handleData(data,dataType);
		dataAll = chartsData;
		var seriesList = [];
		var nameList = [];
		chartsData.dataList.forEach(function (cur,idx,arr){
			var seriesData = {
				data: cur.list,
				name: cur.name,
				type: 'bar',
				barMaxWidth: 60,
				showBackground: true,
				backgroundStyle: {
					color: 'rgba(180, 180, 180, 0.2)'
				}
			}
			nameList.push(cur.name);
			seriesList.push(seriesData);
		})
		var myChart=echarts.init(document.getElementById(module+'charts'),layui.webframe.judgeTheme()? 'dark':'');
		var option = {
			tooltip: {
				trigger: 'axis',
				axisPointer: {            // 坐标轴指示器，坐标轴触发有效
					type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend: {
				data: nameList,
				itemGap: 20
			},
			xAxis: {
				type: 'category',
				name: chartsData.originData.xLabel,
				data: chartsData.originData.xAxisOrigin,
				axisTick: {
					alignWithLabel: true
				},
				axisLabel: {
					interval: 0,
					// width: '100%',
					// overflow: "break"
					// rotate:40
					formatter : function (params,idx){
						return renderecharts.handleXLabel(params,idx);
					}
				}
			},
			yAxis: {
				type: 'value',
				name: chartsData.originData.yLabel,
				minInterval: 1,
				axisLine: {
					show: true
				},
			},
			series: seriesList
		};
		myChart.setOption(option,true);
	}

	renderecharts.line = function (data,dataType) {
		var chartsData = renderecharts.handleData(data,dataType);
		dataAll = chartsData;
		var seriesList = [];
		var nameList = [];
		chartsData.dataList.forEach(function (cur,idx,arr){
			var seriesData = {
				data: cur.list,
				name: cur.name,
				type: 'line'
			}
			nameList.push(cur.name);
			seriesList.push(seriesData);
		})
		var myChart=echarts.init(document.getElementById(module+'charts'));
		var option = {
			tooltip: {
				trigger: 'axis'
			},
			legend: {
				data: nameList
			},
			xAxis: {
				name: chartsData.originData.xLabel,
				type: 'category',
				data: chartsData.originData.xAxisOrigin,
				boundaryGap: true,
				axisTick: {
					alignWithLabel: true
				},
				axisLabel: {
					interval: 0,
					margin: 10,
					alignWithLabel: true,
					textStyle: {
						fontSize: 14,
						color : '#1892ff'
					},
					formatter : function (params){
						return renderecharts.handleXLabel(params);
					}
				},
			},
			yAxis: {
				name: chartsData.originData.yLabel,
				type: 'value',
				minInterval: 1,
				axisLine: {
					show: true
				},
			},
			series: seriesList
		};
		myChart.setOption(option,true);
	}

	renderecharts.column2d = function (data,dataType) {
		var chartsData = renderecharts.handleData(data,dataType);
		dataAll = chartsData;
		var seriesList = [];
		var nameList = [];
		chartsData.dataList.forEach(function (cur,idx,arr){
			var seriesData = {
				data: cur.list,
				name: cur.name,
				type: 'bar',
				barMaxWidth: 60,
				showBackground: true,
				backgroundStyle: {
					color: 'rgba(180, 180, 180, 0.2)'
				}
			}
			nameList.push(cur.name);
			seriesList.push(seriesData);
		})
		var myChart=echarts.init(document.getElementById(module+'charts'));
		var option = {
			tooltip: {
				trigger: 'axis',
				axisPointer: {
					type: 'shadow'
				}
			},
			legend: {
				data: nameList,
				itemGap: 20 ,
			},
			grid: {
				left: '3%',
				right: '4%',
				bottom: '3%',
				containLabel: true
			},
			xAxis: {
				name: chartsData.originData.yLabel,
				type: 'value',
				minInterval: 1,
				boundaryGap: [0, 0.01],
				axisLabel: {
					interval: 0,
					margin: 10,
					alignWithLabel: true,
					textStyle: {
						fontSize: 14,
						color : '#1892ff'
					},
					formatter : function (params){
						return renderecharts.handleXLabel(params);
					}
				},
				axisLine: {
					show: true
				},
			},
			yAxis: {
				name: chartsData.originData.xLabel,
				type: 'category',
				data: chartsData.originData.xAxisOrigin,
				axisTick: {
					alignWithLabel: true
				}
			},
			series: seriesList
		};
		myChart.setOption(option,true);
	}

	renderecharts.area2d = function (data,dataType) {
		var chartsData = renderecharts.handleData(data,dataType);
		dataAll = chartsData;
		var seriesList = [];
		var nameList = [];
		chartsData.dataList.forEach(function (cur,idx,arr){
			var seriesData = {
				data: cur.list,
				name: cur.name,
				type: 'line',
				areaStyle: {}
			}
			nameList.push(cur.name);
			seriesList.push(seriesData);
		})
		var myChart=echarts.init(document.getElementById(module+'charts'));
		var option = {
			tooltip: {
				trigger: 'axis',
				axisPointer: {
					type: 'shadow',
				}
			},
			legend: {
				data: nameList,
				itemGap: 20 ,
			},
			xAxis: {
				name: chartsData.originData.xLabel,
				type: 'category',
				data: chartsData.originData.xAxisOrigin,
				axisLabel: {
					interval: 0,
					formatter : function (params){
						return renderecharts.handleXLabel(params);
					}
				},
				boundaryGap: true,
				axisTick: {
					alignWithLabel: true
				},
			},
			yAxis: {
				name: chartsData.originData.yLabel,
				type: 'value',
				minInterval: 1,
				axisLine: {
					show: true
				},
			},
			series: seriesList
		};
		myChart.setOption(option,true);
	}

	renderecharts.pie3d = function (data) {
		var loadPie3d = function (){
			var chart = Highcharts.chart(module+'charts', {
				chart: {
					type: 'pie',
					options3d: {
						enabled: true,
						alpha: 45,
						beta: 0
					}
				},
				title: {
					text: '2014年某网站不同浏览器访问量占比'
				},
				tooltip: {
					pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
				},
				plotOptions: {
					pie: {
						allowPointSelect: true,
						cursor: 'pointer',
						depth: 35,
						dataLabels: {
							enabled: true,
							format: '{point.name}'
						}
					}
				},
				series: [{
					type: 'pie',
					name: '浏览器占比',
					data: [
						['Firefox',   45.0],
						['IE',       26.8],
						{
							name: 'Chrome',
							y: 12.8,
							sliced: true,
							selected: true
						},
						['Safari',    8.5],
						['Opera',     6.2],
						['Others',   0.7]
					]
				}]
			});
		}
	}

	renderecharts.pie2d = function (data,dataType) {
		var chartsData = renderecharts.handleData(data,dataType);
		var seriesList = [];
		chartsData.dataList[0].list.forEach(function (cur,idx,arr){
			var seriesData = {
				value: cur,
				name: chartsData.originData.xAxisOrigin[idx]
			}
			seriesList.push(seriesData);
		})
		var myChart=echarts.init(document.getElementById(module+'charts'));
		var option = {
			tooltip: {
				trigger: 'item'
			},
			legend: {
				orient: 'vertical',
				left: 'left',
			},
			series: [
				{
					name: '',
					type: 'pie',
					radius: '70%',
					data: seriesList,
					emphasis: {
						itemStyle: {
							shadowBlur: 10,
							shadowOffsetX: 0,
							shadowColor: 'rgba(0, 0, 0, 0.5)'
						}
					}
				}
			]
		};
		myChart.setOption(option,true);
	}


	renderecharts.ring = function (data,dataType) {
		var chartsData = renderecharts.handleData(data,dataType);
		var seriesList = [];
		chartsData.dataList[0].list.forEach(function (cur,idx,arr){
			var seriesData = {
				value: cur,
				name: chartsData.originData.xAxisOrigin[idx]
			}
			seriesList.push(seriesData);
		})
		var myChart=echarts.init(document.getElementById(module+'charts'));
		var option = {
			tooltip: {
				trigger: 'item'
			},
			legend: {
				orient: 'vertical',
				left: 'left',
			},
			series: [
				{
					name: '',
					type: 'pie',
					radius: ['50%', '70%'],
					avoidLabelOverlap: false,
					label: {
						show: false,
						position: 'center'
					},
					emphasis: {
						label: {
							show: true,
							fontSize: '20',
							fontWeight: 'bold'
						}
					},
					labelLine: {
						show: false
					},
					data: seriesList
				}
			]
		};
		myChart.setOption(option,true);
	}


	//处理X轴标签方法
	renderecharts.handleXLabel = function(params,idx){

		var chartsWidth = Number($('#'+module+'charts').width())-200;
		var xLength = dataAll.originData.xAxisOrigin.length;
		var singleWidth = Math.ceil(chartsWidth / xLength);

		if(singleWidth<60){
			var provideNumber = 4;
		}else if(singleWidth>=60 && singleWidth<100){
			var provideNumber = 6;
		}else if(singleWidth>=100 && singleWidth<140){
			var provideNumber = 8;
		}else if(singleWidth>=140){
			var provideNumber = 10;
		}

		var newParamsName = ""; // 最终拼接成的字符串
		var paramsNameNumber = params.length; // 实际标签的个数
		// var provideNumber = 8;  // 每行能显示的字的个数
		var rowNumber = Math.ceil(paramsNameNumber / provideNumber); // 换行的话，需要显示几行，向上取整
		/**
		 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
		 */
		// 条件等同于rowNumber>1
		if (paramsNameNumber > provideNumber) {
			/** 循环每一行,p表示行 */
			for (var p = 0; p < rowNumber; p++) {
				var tempStr = "";// 表示每一次截取的字符串 x
				var start = p * provideNumber;// 开始截取的位置
				var end = start + provideNumber;// 结束截取的位置
				// 此处特殊处理最后一行的索引值
				if (p == rowNumber - 1) {
					// 最后一次不换行
					tempStr = params.substring(start, paramsNameNumber);
				} else {
					// 每一次拼接字符串并换行x
					tempStr = params.substring(start, end) + "\n";
				}
				newParamsName += tempStr;// 最终拼成的字符串
			}
		} else {
			// 将旧标签的值赋给新标签
			newParamsName = params;
		}
		//将最终的字符串返回
		return newParamsName
	}


	renderecharts.handleData = function (data){
		var returnData = {
			originData: data,
			dataList: []
		}
		// if(data.seriesOrigin || data.xAxisOrigin) return false;
		if(data.dataType=='single'){
			var dataObjSingle = {
				name: '',
				list: data.seriesOrigin
			}
			returnData.dataList.push(dataObjSingle);
		}else if(data.dataType=='multi'){
			data.seriesOrigin.forEach(function (cur,idx,arr){
				for(var key in JSON.parse(cur)){
					var dataObjMmulti = {
						name: key,
						list: JSON.parse(cur)[key]
					}
				}
				returnData.dataList.push(dataObjMmulti);
			});
		}
		return returnData;
	}

	exports('renderecharts', renderecharts)
});
