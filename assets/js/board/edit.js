
function tinymce_editor(){
	tinymce.init({
	
	    selector: '#editor',
		forced_root_block: 'p',
	    height: 500, // 원하는 편집기 높이 설정
	    menubar: false, // 기본 메뉴바 제거
		plugins: 'image link | image | media | link',
	    toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist | outdent indent | forecolor backcolor | media | link',
		relative_urls: false, // 상대 URL 사용 비활성화
	    remove_script_host: false, // 스크립트 호스트 제거 비활성화
		content_css: `/assets/css/board/custom_tinymce.css`,
		setup: function (editor) {
	        editor.on('change', function () {
	            // 에디터 내용이 변경될 때 호출되는 함수
	            adjustYouTubeIframes(editor);
	        });
	    }
	  });
 }
 
 
   function adjustYouTubeIframes(editor) {
    // 에디터 내용에서 모든 YouTube iframe을 선택
    var iframes = editor.dom.select('iframe[src*="youtube.com"]');
  
    
    for (var i = 0; i < iframes.length; i++) {
        var iframe = iframes[i];
        
        var isMobile = window.innerWidth < 768;
        
        if (isMobile) {
		    iframe.setAttribute('width', '100%'); // 너비를 560으로 설정
        	iframe.removeAttribute('height'); // 높이 속성 제거 (반응형으로 조정)
		} else {
	        iframe.setAttribute('width', '560'); // 너비를 560으로 설정
	        iframe.setAttribute('height', '380'); //높이를 380으로 설정
        //iframe.removeAttribute('height'); // 높이 속성 제거 (반응형으로 조정)
        }
    }
  }
  
  window.addEventListener('DOMContentLoaded', function() {
    tinymce_editor();
});
/*
function uploadFile() {
    const fileInput = document.getElementById('file_input');
    const file = fileInput.files[0];

    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        $.ajax({
            type: 'POST',
            url: './FileUpload',
            data: formData,
            contentType: false,
            processData: false,
            enctype: 'multipart/form-data',
            dataType: 'text',
            success: function (data) {
				console.log("data => " + data);
                // 파일 업로드 성공 시, 파일 URL을 텍스트 영역에 삽입
                const editor = tinymce.get('editor');
                editor.insertContent(`<img src="${data}" alt="Uploaded Image"/>`);
            },
            error: function (error) {
                console.error('파일 업로드 실패.', error);
            }
        });
    } else {
        console.error('파일을 선택하세요.');
    }
}*/




function uploadFile() {
    const fileInput = document.getElementById('file_input');
    const files = fileInput.files;

    if (files.length > 0) {
		showUploadModal();
        const formData = new FormData();
        
        for (let i = 0; i < files.length; i++) {
        	formData.append('files', files[i]);
        }

        $.ajax({
            type: 'POST',
            url: contextPath + '/board/write/MultipleFileUpload',
            data: formData,
            contentType: false,
            processData: false,
            enctype: 'multipart/form-data',
            dataType: 'json',
            xhr: function () {
                const xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener("progress", function (evt) {
                    if (evt.lengthComputable) {
                        const percentComplete = (evt.loaded / evt.total) * 100;
                                updateProgressBar(percentComplete);
                    }
                }, false);
                return xhr;``
            },
			
            success: function (data) {
				console.log("on edit page =>", data);
                // 파일 업로드 성공 시, 파일 URL을 텍스트 영역에 삽입
                const editor = tinymce.get('editor');
                data.forEach(fileUrl => {
					
				let extension = fileUrl.split('.').pop().split(' ')[0].toLowerCase();
					
		            let isVideo = ['mp4', 'webm', 'ogg'].indexOf(extension) > -1; // 동영상 확장자 추가
		            let tag = isVideo ? 
		                `<p style="text-align: center;"><video width="540" height="360" controls><source src="${fileUrl}">Your browser does not support the video tag.</video></p>` : 
		                `<p style="text-align: center;"><img src="${fileUrl}" alt="Uploaded Image" style="max-width: 100%; height: auto"/></p>`;
		            editor.insertContent(tag);
		            fileInputChange();
		        });
		         hideUploadModal();
		    },
            error: function (error) {
                console.error('파일 업로드 실패.', error);
                hideUploadModal();
            }
        });
    } else {
        console.error('파일을 선택하세요.');
    }
}


function fileInputChange()
{
	  document.getElementById('file_input').addEventListener('change', handleFileSelect);

        function handleFileSelect(event) {
            const files = event.target.files;
            const fileInputLabel = document.getElementById('fileInputLabel');
            const selectedFilesDiv = document.getElementById('selectedFiles');

            if (files.length > 0) {
                fileInputLabel.innerHTML = `${files.length}개 파일 선택됨`;

                // 파일 이름들을 표시
                selectedFilesDiv.innerHTML = '';
                for (let i = 0; i < files.length; i++) {
                    const fileNameDiv = document.createElement('div');
                    fileNameDiv.textContent = files[i].name;
                    selectedFilesDiv.appendChild(fileNameDiv);
                }
            } else {
                fileInputLabel.innerHTML = '파일 선택';
                selectedFilesDiv.innerHTML = '';
            }
        }
}
function fileUploadChange() {
    document.getElementById('uploadFileBtn').click();

    const fileInput = document.getElementById('file_input');
    const filePreviewsContainer = document.getElementById('file_previews');

    for (let i = 0; i < fileInput.files.length; i++) {
        const file = fileInput.files[i];
        const preview = document.createElement('div');
        preview.classList.add('file-preview');

        // Check if it's an image
        if (file.type.startsWith('image/')) {
            const imgContainer = document.createElement('div');
            const img = document.createElement('img');
            const toggleConfirmation = document.createElement('div');

            img.style.width = '60px';
            img.style.height = '60px';
            img.src = URL.createObjectURL(file);
            img.alt = 'Image Preview';

            toggleConfirmation.classList.add('toggle-confirmation');
            toggleConfirmation.innerText = '✓';

            imgContainer.appendChild(img);
            //imgContainer.appendChild(toggleConfirmation);

            imgContainer.addEventListener('click', function () {
                togglePreviewSelection(imgContainer);
            });

            preview.appendChild(imgContainer);
        }
        // Check if it's a video
        else if (file.type.startsWith('video/')) {
            const videoContainer = document.createElement('div');
            const video = document.createElement('video');
            const toggleConfirmation = document.createElement('div');

            video.src = URL.createObjectURL(file);
            video.style.width = '60px';
            video.style.height = '60px';
            video.controls = true;

            toggleConfirmation.classList.add('toggle-confirmation');
            toggleConfirmation.innerText = '✓';

            videoContainer.appendChild(video);
            videoContainer.appendChild(toggleConfirmation);

            videoContainer.addEventListener('click', function () {
                togglePreviewSelection(videoContainer);
            });

            preview.appendChild(videoContainer);
        }

        filePreviewsContainer.appendChild(preview);
    }
}

function togglePreviewSelection(element) {
    element.classList.toggle('selected');
}

function deleteSelectionPreviews() {
    const selectedPreviews = document.querySelectorAll('.file-preview.selected');
    selectedPreviews.forEach(preview => {
        preview.remove();
    });
}
function deleteAllPreviews(){
	const filePreviewsContainer = document.getElementById('file_previews');
	const allPreviews = filePreviewsContainer.querySelectorAll('.file-preview');
	
	allPreviews.forEach(preview => {
		const fileUrl = getFileUrlFromPreview(preview);

		if (fileUrl) {
			deleteFileContent(fileUrl);
		}
	});
	filePreviewsContainer.innerHTML='';
}

function getFileUrlFromPreview(preview) {
    const img = preview.querySelector('img');
    const video = preview.querySelector('video');
    return img ? img.src : (video ? video.src : null);
}

function deleteFileContent(fileUrl) {

    const editor = tinymce.get('editor');
    if (editor) {
        const content = editor.getContent();
        const doc = new DOMParser().parseFromString(content, 'text/html');
        
        const images = doc.querySelectorAll('img[src="' + fileUrl + '"]');
        images.forEach(img => {
			console.log('Removing image:', img);
            img.remove();
        });

        const updatedContent = doc.body.innerHTML;
        editor.setContent(updatedContent);
    }
}
function escapeRegExp(string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
}
