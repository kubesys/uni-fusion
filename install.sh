#! /bin/bash
###############################################
##
##  Copyright (2023, ) Institute of Software
##      Chinese Academy of Sciences
##          wuheng@iscas.ac.cn
##
###############################################


VERSION="0.2"

third_parts=("kube-database"  "kube-message")

#our_system=("kube-mirror")
our_system=("kube-mirror" "kube-backend")

database=("kubestack" "kubeauth")

function wait-ready()
{
  while true
  do
    status=$(kubectl get po -A | grep "$1" | awk '{print$4}')
    if [[ $status = "Running" ]]
    then
      echo "$1 is ready"
      break
    fi
    echo "$1 is not ready, wait for 5 seconds.."
    sleep 5
  done
}

function create-database()
{
  pod_name=$(kubectl get po -A | grep kube-database | awk '{print$2}')
  kubectl exec -it $pod_name -n kube-stack -- psql -h 127.0.0.1 -U postgres -d postgres -c "CREATE DATABASE $1;"
}

for pod in "${third_parts[@]}"
do
    wait-ready $pod
done

for db in "${database[@]}"
do
    create-database $db
done

for pod in "${our_system[@]}"
do
    wait-for-ready $pod
done

pod_name=$(kubectl get po -A | grep kube-database | awk '{print$2}') 
timestamp=$(date +"%Y-%m-%d %H:%M:%S.%6N")
 
#
kubectl exec -it $pod_name -n kube-stack -- psql -h 127.0.0.1 -U postgres -d kubeauth -c "INSERT INTO \"basic_user\" (\"name\", \"createdat\", \"updatedat\", \"password\", \"role\", \"token\") VALUES('admin','$timestamp','$timestamp','b25jZWFzCg==', 'admin','');"
#
IP=$(cat /root/.kube/config | grep server | awk '{print$2}')
TOKEN=$(kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep kubernetes-client | awk '{print $1}') | grep "token:" | awk -F":" '{print$2}' | sed 's/ //g')
kubectl exec -it $pod_name -n kube-stack -- psql -h 127.0.0.1 -U postgres -d kubeauth -c "INSERT INTO \"basic_role\" (\"role\", \"createdat\", \"updatedat\", \"allows\", \"tokens\") VALUES('admin','$timestamp','$timestamp','{\"all\": {}}','{\"local\": {\"url\": \"$IP\",\"token\": \"$TOKEN\"}}');"

