base=$(pwd)
version="24.01"

for name in `ls | grep .yaml`
do
  curl -T $base/$name -u wuheng@iscas.ac.cn "https://g-ubjg5602-generic.pkg.coding.net/iscas-system/files/$name?version=$version"
done
