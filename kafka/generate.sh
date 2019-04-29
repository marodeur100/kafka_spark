#!/bin/bash

SCRIPTPATH="`dirname "$0"`"
echo $SCRIPTPATH

echo "(Re-)Create customer_transaction table within legacy-db"
POD_NAME=$(sudo kubectl get pods -l "name=legacy-db" -n stream -o jsonpath="{.items[0].metadata.name}")
echo "Pod name: $POD_NAME" 
sudo kubectl cp create_transaction_table.sql $POD_NAME:/ -n stream
sudo kubectl exec -it $POD_NAME -n stream  psql legacy < create_transaction_table.sql

for (( ; ; ))
do

	
   
	declare -a actions=('login' 'checked_balance' 'cash_transfered'            'creditcard_payment'  'contacted_support'  'logout')
	selected_action=${actions[$(($RANDOM % ${#actions[@]}))]}

	declare -a users=('DarthVader' 'C-3PO'  'Chewbacca'  'HanSolo'  'LukeSkyWalker'  'ObiWan'  'R2D2'  'Yoda'  'SpiderMan'  'IronMan'  'DeadPool'  'Thor'  'Hulk')
	index=$(($RANDOM % ${#users[@]}))
	selected_user=${users[index]}
	selected_userid=($index + 1)
	ts=$(date)

	cp insert_transaction.sql.template insert_transaction.sql
	sed -i -e 's/'@userid@'/'"$selected_userid"'/g' insert_transaction.sql
	sed -i -e 's/'@action@'/'"$selected_action"'/g' insert_transaction.sql
	sed -i -e 's/'@username@'/'"$selected_user"'/g' insert_transaction.sql		

	sudo kubectl cp insert_transaction.sql $POD_NAME:/ -n stream
	sudo kubectl exec -it $POD_NAME -n stream  psql legacy < insert_transaction.sql

	echo "New Event generated: $selected_action, $selected_user, $selected_userid, $ts" 
	sleep 1
done
