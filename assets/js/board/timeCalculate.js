function formatDateTimeDifference(mysqlDatetime) {
    var outputDateFormat = new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric',
        timeZone: 'Asia/Seoul'
    });

    var currentDate = new Date();
    var mysqlDate = new Date(mysqlDatetime);

    var timeDifference = currentDate.getTime() - mysqlDate.getTime();
	console.log(timeDifference, "시간 차이 => 현재 시간, ", currentDate.getTime(), ", mysqlDate.getTime",mysqlDate.getTime())
    var secondsDifference = Math.floor(timeDifference / 1000);
    var minutesDifference = Math.floor(secondsDifference / 60);
    var hoursDifference = Math.floor(minutesDifference / 60);
    var daysDifference = Math.floor(hoursDifference / 24);

	if (daysDifference <0 && hoursDifference < 0 && minutesDifference < 0)
	{
		return '방금 전';
	}
    if (daysDifference === 0) {
        if (hoursDifference === 0) {
            if (minutesDifference === 0) {
                return '방금 전';
            } else {
                return minutesDifference + '분 전';
            }
        } else {
            return hoursDifference + '시간 전';
        }
    } else if (daysDifference === 1) {
        return '1일 전';
    } else if (daysDifference <= 7) {
        return daysDifference + '일 전';
    } else {

        return outputDateFormat.format(mysqlDate);
    }
}

