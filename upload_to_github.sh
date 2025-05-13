#!/bin/bash

# 初始化 Git 儲存庫
git init

# 添加遠程儲存庫
git remote add origin https://github.com/cis2042/gamm3.git

# 添加所有文件到 Git
git add .

# 提交更改
git commit -m "Initial commit: Gemma Messenger app"

# 推送到 GitHub
git push -u origin master  # 如果默認分支是 'main'，請將 'master' 改為 'main'

echo "上傳完成！請檢查 https://github.com/cis2042/gamm3"
