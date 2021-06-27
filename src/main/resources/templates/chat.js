//message["senderU", "receiverU", "sender", "message", "time", "sender", "message", "time", "sender", "message", "time"]


function loopTest(message, k){
	var i;
	for (i=2; i< k;) { 
	
			var newDiv = document.createElement("div");
			var newImg = document.createElement("img");
			var newSpan = document.createElement("span");
			var newP = document.createElement("p");
			
		if(message[i] == message[0])
		{
			console.log("Ananas");
			newP.classList.add("chatrighttext");
			newSpan.classList.add("time-left");
			newDiv.classList.add("container");
			newDiv.classList.add("darker");
			newImg.classList.add("right");
			newImg.src="/w3images/bandmember.jpg";
			newImg.alt="Avatar";
			newImg.style="width:100%;";
			

			// and give it some content
			var newContent = document.createTextNode(message[i+1]);
			var newContentSpan = document.createTextNode(message[i+2]);
			i+=3;
			console.log("Ananas");
		}
		else {
			
			newP.classList.add("chatlefttext");
			newSpan.classList.add("time-right");
			newDiv.classList.add("container");
			newImg.src="/w3images/bandmember.jpg";
			newImg.alt="Avatar";
			newImg.style="width:100%;";
			

			// and give it some content
			var newContent = document.createTextNode(message[i+1]);
			var newContentSpan = document.createTextNode(message[i+2]);
			i+=3;
			console.log("Apple");
		}

		// add the text node to the newly created div
		newDiv.appendChild(newImg);
		newP.appendChild(newContent);
		newDiv.appendChild(newP);
		newSpan.appendChild(newContentSpan);
		newDiv.appendChild(newSpan);

		// add the newly created element and its content into the DOM
		var currentDiv = document.getElementById("div1");
		currentDiv.appendChild(newDiv);
		
	}
}