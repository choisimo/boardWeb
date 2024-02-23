    document.getElementById('openModalBtn').addEventListener('click', openModal);

    function openModal() {
        document.getElementById('myModal').style.display = 'flex';
    }

    function closeModal() {
        document.getElementById('myModal').style.display = 'none';
    }
    
    function searchAction() {
		
		var so = document.getElementById('searchOption').value;
		var sv = document.getElementById('search_value_input').value;
		pageNum = 1;
		location.href = contextPath + `/search/n/${pageNum}?so=${so}&sv=${sv}`;
		//location.href = contextPath + `/search/n/${pageNum.replace(/\/$/, '')}/?so=${so}&sv=${sv}`;
	}