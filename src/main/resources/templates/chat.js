function loopTest(){
	var i=0;
	for (i=0;i<5;i++) {  
        var newDiv = document.createElement("div");
		var newImg = document.createElement("img");
		var newSpan = document.createElement("span");
		
		newSpan.classList.add("time-right");
		newDiv.classList.add("container");
		newImg.src="/w3images/bandmember.jpg";
		newImg.alt="Avatar";
		newImg.style="width:100%;";
		

		// and give it some content
		var newContent = document.createTextNode("Hi there and greetings!");
		var newContentSpan = document.createTextNode("19:13");

		// add the text node to the newly created div
		newDiv.appendChild(newImg);
		newDiv.appendChild(newContent);
		newSpan.appendChild(newContentSpan);
		newDiv.appendChild(newSpan);

		// add the newly created element and its content into the DOM
		var currentDiv = document.getElementById("div1");
		currentDiv.appendChild(newDiv);
		
	}
}