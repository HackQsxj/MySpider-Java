# MySpider
使用Java实现的，可以爬取新浪、腾讯、CSDN、博客园所发布博客信息的爬虫。

由于新浪微博需要登陆后才能进行微博查看，在此我们使用一个新浪账号进行模拟登陆后，再进行爬取；其他博客无此要求，可以直接爬取。
新浪微博账号配置文件为：conf/loginInfo.properties
将账号，密码配置后即可使用。

使用过程：
1. 首先，输入要爬取的博客地址，以单个博客为一行，最后输入“END”结束。
示例：

https://weibo.com/liuyifeiofficial

https://www.cnblogs.com/liushilin/

https://blog.csdn.net/HackQ_sxj

END

2. 然后程序运行，输入爬取的页数后，即可自动进行爬取。

