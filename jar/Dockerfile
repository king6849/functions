# 基于这个镜像构建
FROM java:8
# 作者
MAINTAINER lian jin
RUN mkdir /usr/app
# 创建图片上传的路径
RUN mkdir /usr/app/upload
# 复制程序到内部
add start-0.0.1-SNAPSHOT.jar excel-1.0-SNAPSHOT.jar common-1.0-SNAPSHOT.jar /usr/app
# 切换工作目录
WORKDIR /usr/app
# 暴露端口
EXPOSE 20000
# 镜像运行时执行
ENTRYPOINT ["java","-jar","start-0.0.1-SNAPSHOT.jar"]