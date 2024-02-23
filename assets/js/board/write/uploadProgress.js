function showUploadModal() {
    $('#uploadModal').show();
}

function hideUploadModal() {
    $('#uploadModal').hide();
}

function updateProgressBar(percentComplete) {
    const roundedPercent = Math.floor(percentComplete);

        const opacity = percentComplete / 100;
		
		$('.showPercent').text(roundedPercent + '%');
        $('#fileUploadProgress .loaded').css('width', roundedPercent + '%');
        
        const fontSize = 2 + (percentComplete / 95);

        $('#progressText').css({
            'font-size': fontSize,
            'opacity': opacity
        });
}



