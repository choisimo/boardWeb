function Memberupload() {
		    
    const fileInput = document.getElementById('imageInput');
    const file = fileInput.files[0];

    if (file) {
        const formData = new FormData();
        formData.append('file', file);
		
		let url = contextPath+`/login/RegisterMemberImg`;
        $.ajax({
            type: 'POST',
            url: url,
            data: formData,
            contentType: false,
            processData: false,
            enctype: 'multipart/form-data',
            dataType: 'text',
            success: function (data) {

    		     const imgElement = document.getElementById('memberEdit_main_img').querySelector('img');
		 	     imgElement.src = contextPath + data;
		  	 	 imgElement.alt = `member_img`;
				
				$('#login-form').append('<input type="hidden" name="mem_profile_img" value="' + contextPath + data + '">'); 
			
            },
            error: function (error) {
                console.error('파일 업로드 실패.', error);
            }
        });
    } else {
        console.error('파일을 선택하세요.');
    }
}

$(document).ready(function() {
    // Function to check if a string is empty or contains only whitespace
    function isEmpty(str) {
        return !str.trim();
    }

    // Function to show error message
    function showError(inputField, errorMessage) {
        var errorElement = $("<span class='error-message'></span>").text(errorMessage);
        $(inputField).siblings('.error-message').remove(); // Remove any existing error messages
        $(inputField).after(errorElement);
    }

    // Function to remove error message
    function removeError(inputField) {
        $(inputField).siblings('.error-message').remove();
    }

    // Event listener for form submission
    $("#login-form").submit(function(event) {
        var isValid = true;

        // Validate mem_id
        var memId = $("input[name='mem_id']").val();
        if (isEmpty(memId)) {
            showError("input[name='mem_id']", "닉네임 ID를 입력하세요");
            isValid = false;
        } else {
            removeError("input[name='mem_id']");
        }

        // Validate login_id
        var loginId = $("input[name='login_id']").val();
        if (isEmpty(loginId)) {
            showError("input[name='login_id']", "로그인 전용 ID를 입력하세요");
            isValid = false;
        } else {
            removeError("input[name='login_id']");
        }

        // Validate mem_name
        var memName = $("input[name='mem_name']").val();
        if (isEmpty(memName)) {
            showError("input[name='mem_name']", "이름을 입력하세요");
            isValid = false;
        } else {
            removeError("input[name='mem_name']");
        }

        // Validate mem_pw
        var memPw = $("input[name='mem_pw']").val();
        if (isEmpty(memPw)) {
            showError("input[name='mem_pw']", "비밀번호를 입력하세요");
            isValid = false;
        } else {
            removeError("input[name='mem_pw']");
        }

        // Return false to prevent form submission if validation fails
        if (!isValid) {
            event.preventDefault();
        }
    });

    // Event listener for input fields to remove error messages on input
    $("input[name='mem_id'], input[name='login_id'], input[name='mem_name'], input[name='mem_pw']").on("input", function() {
        removeError(this);
    });
});
