function pushButton () {
    var name = document.getElementById('name').value;
    if(name.length > 0){ 	
    alert("Hey there "+ name + "!!!");
    }else{
    alert("Hey there stranger!!!");
    }
}

function add () {
	var a = parseInt(document.getElementById('num1').value);
	var b = parseInt(document.getElementById('num2').value);
	if (a != null && b != null) {
		alert(a+b);
	}
}
