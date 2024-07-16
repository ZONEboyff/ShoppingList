package com.example.shoppinglist.ui.theme

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.shoppingItem
@Composable
fun ShoppingList(){
    var sItems by remember{ mutableStateOf(listOf<shoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .background(Color.Blue)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {showDialog = true},
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            ){
                Text(text = "ADD",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItems){
                item ->
                if(item.isEditing){
                    EditWindow(item =item , onEditComplete = {
                        editedName,editedQuantity ->
                        sItems = sItems.map{it.copy(isEditing = false)}
                        val editedItem = sItems.find{it.id == item.id}
                        editedItem?.let{
                            it.name = editedName
                            it.qty = editedQuantity
                        }
                    })
                }else{
                    ShoppingListItem(item = item, onEditClick = {
                        sItems = sItems.map { it.copy(isEditing = it.id==item.id)}},
                        onDeleteClick = {
                            sItems = sItems-item
                        })
                }
            }
        }

    }
    if(showDialog){
       AlertDialog(onDismissRequest = {showDialog = false },
           confirmButton = {
                           Row(modifier = Modifier
                               .fillMaxWidth()
                               .padding(8.dp),
                               horizontalArrangement = Arrangement.SpaceBetween) {
                               Button(onClick = {
                                   if(itemName.isNotBlank()){
                                       val item =shoppingItem(id=sItems.size+1, name = itemName)
                                       if(itemQuantity.isNotBlank()) item.qty=itemQuantity.toInt()
                                       sItems+=item
                                       showDialog=false
                                       itemName=""
                                       itemQuantity=""
                                   }
                               }, enabled = itemName.isNotBlank()
                                   ) {
                                   Text(text = "ADD")
                               }
                               Button(onClick = {showDialog = false}) {
                                   Text(text = "CANCEL")
                               }

                           }
           },
           title = { Text(text = "ADD ITEM")},
           text = {
                Column {
                    OutlinedTextField(value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = {Text(text = "Item Name")})
                    OutlinedTextField(value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = {Text(text = "Quantity")},
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ))
                }
           }
           )
    }
}
@Composable
fun ShoppingListItem(item:shoppingItem,
                     onEditClick:()-> Unit,
                     onDeleteClick:()->Unit){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(border = BorderStroke(2.dp, color = Color.Blue), shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text =item.name,modifier = Modifier.padding(8.dp))
            Text(text="Qty: ${item.qty}", modifier = Modifier.padding(8.dp))
            Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = onEditClick){
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }
}
@Composable
fun EditWindow(item: shoppingItem, onEditComplete:(String, Int)->Unit){
    var editedName by remember {mutableStateOf(item.name)}
    var editedQuantity by remember {mutableStateOf(item.qty.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing) }
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Blue)){
        Column(modifier = Modifier.padding(8.dp)){
            TextField(value = editedName, onValueChange = {editedName = it})
            TextField(value = editedQuantity, onValueChange = {editedQuantity= it},keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ))
        }
        Button(onClick = {onEditComplete(editedName,editedQuantity.toInt())}) {
            Text(text="SAVE")
        }
    }

}
@Preview(showBackground = true)
@Composable
fun ShoppingListPreview(){
    ShoppingList()

}