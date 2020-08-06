export function validateEmail(value) {
	const re = /\S+@\S+\.\S+/;
	return re.test(value);
}

export function validatePassword(value) {
	const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
	return re.test(value);
}

export function validateMarkdown(value) {
	const re1 = /[^a-zA-Zㄱ-힣0-9<>.]/g;
	value.replace(re1,' ');
	const re2 = /!\[(.*?)\)/;
	value = value.replace(re2,'');
	const re5 = /<(.*?).jpg\)/;
	value = value.replace(re5,'');
	const re6 = /<(.*?).png\)/;
	value = value.replace(re6,'');

	const re7 = /!\[(.*?)\)/gi;
	value = value.replace(re7,'');
	const re8 = /<(.*?)>/gi;
	value = value.replace(re8,'');

	const re9 = /\((.*?)(http(s)?:\/\/)([a-z0-9\w]+\.*)+[a-z0-9]{2,4}(\?|\/)?(.*?)\)/gi;
	value = value.replace(re9,'');
	const re10 = /\[(.*?)(http(s)?:\/\/)([a-z0-9\w]+\.*)+[a-z0-9]{2,4}(\?|\/)?(.*?)\]/gi;
	value = value.replace(re10,'');
	const re11 = /<(http(s)?:\/\/)([a-z0-9\w]+\.*)+[a-z0-9]{2,4}(\?|\/)?(.*?)>/gi;
	value = value.replace(re11,'');
	const re12 = /(http(s)?:\/\/)([a-z0-9\w]+\.*)+[a-z0-9]{2,4}(\?|\/)?(.*?)/gi;
	value = value.replace(re12,'');

	return value;
}