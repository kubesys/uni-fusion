#! /bin/bash
###############################################
##
##  Copyright (2023, ) Institute of Software
##      Chinese Academy of Sciences
##          wuheng@iscas.ac.cn
##
###############################################


VERSION="24.02"

third_parts=("prometheus" "loki" "grafana" "jenkins")

for name in "${third_parts[@]}"; do
  curl -fL "https://g-ubjg5602-generic.pkg.coding.net/iscas-system/files/$name-pv.yaml?version=$VERSION" -o $name-pv.yaml
  curl -fL "https://g-ubjg5602-generic.pkg.coding.net/iscas-system/files/$name.yaml?version=$VERSION" -o $name.yaml
  kubectl apply -f $name-pv.yaml
  kubectl apply -f $name.yaml
done

