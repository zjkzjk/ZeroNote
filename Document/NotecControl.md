#NotecControl控制器介绍

##介绍总览

	本控制器负责笔记本相关的功能

##接口

###接口总览
|           接口           |  方法  |  数据  |  返回  |  作用   |
| :--------------------: | :--: | :--: | :--: | :---: |
|  /api/Notec/addNotec   | post | json | json | 添加笔记本 |
| /api/Notec/deleteNotec | get  |  url  | json | 删除笔记本 |
|  /api/Notec/moveNotec  | post | json | json | 移动笔记本 |
|  /api/Notec/getNotec   | get  |  url  | json | 获取笔记本 |
| /api/Notec/editorNotec | post | json | json | 修改笔记本 |

###接口详情

####/api/Notec/addNotec

方法：POST

数据样例：

```
data:{
	"userid":"17854290633",
	"notec_name":"测试",
	"notec_desc":"这是一个测试分类",
	"pic":"/pic"
}
```

返回样例： `{ "result" : 代码}`

|  代码  |       含义        |
| :--: | :-------------: |
|  0   |    失败，笔记本存在     |
|  1   |       成功        |
|  2   | 失败，网络错误 |

####/api/Notec/getNotec

方法：GET

数据样例：
```
url: /api/Notec/getNotec?userid=17854290633
```

返回样例：

```
data:{
	"array":[
		{
			"notec_id":"1",
			"notec_name":"测试",
			"notec_desc":"这是一个测试分类",
			"updatetime":"2017-11-06 11:12",
			"pic":"/pic"
		},
		{
			"notec_id":"2",
			"notec_name":"测试2",
			"notec_desc":"这是一个测试分类2",
			"updatetime":"2017-11-06 11:12"
			"pic":"/pic"
		},
		...
	],
	"count":5 //笔记本数量
	
}
```

####/api/Notec/deleteNotec

方法：GET

数据样例：

```
url:/api/Notec/deleteNotec?notec_id=1
```

返回样例：`{ "result" : 代码}`

|  代码  |       含义        |
| :--: | :----------------: |
|  0   |    失败，笔记本不存在     |
|  1   |       成功        |
|  2   | 失败，网络错误    |

####/api/Notec/editorNotec

方法：POST

数据样例：

```
data:{
	"notec_id":"1"，
	"notec_name":"测试2",
	"notec_desc":"这是一个测试分类2",
	"pic":"/pic"
}
```

返回样例： `{ "result" : 代码}`

|  代码 |       含义         |
| :--: | :-------------:   |
|  0   |    失败，笔记本不存在|
|  1   |       成功         |
|  2   |    失败，网络错误    |

####/api/Notec/moveNotec

方法：POST

数据样例：

```
data:{
	"notec_id":"1",
	"moveToId":"2"
}
```
返回样例： `{ "result" : 代码}`

|  代码 |       含义         |
| :--: | :-------------:   |
|  0   |    失败，笔记本不存在|
|  1   |       成功         |
|  2   |    失败，网络错误    |
