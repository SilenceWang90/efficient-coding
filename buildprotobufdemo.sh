# 将当前build.sh脚本所在目录中，proto目录下的user.proto文件编译输出到build.sh脚本所在目录中/src/main/java/目录下
./protoc ./proto/protobufdemo.proto --java_out=./src/main/java/

# 等待用户按回车键继续
read -p "Press [Enter] key to continue..."