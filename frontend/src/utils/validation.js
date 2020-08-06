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
	const re3 = /<img(.*?)>/;
	value = value.replace(re3,'');
	const re4 = /<\/img(.*?)>/;
	value = value.replace(re4,'');
	const re5 = /<(.*?).jpg\)/;
	value = value.replace(re5,'');
	const re6 = /<(.*?).png\)/;
	value = value.replace(re6,'');
	const re7 = /!\[(.*?)\)/;
	value = value.replace(re7,'');
	const re8 = /!\[(.*?)\)/;
	value = value.replace(re8,'');
	const re9 = /!\[(.*?)\)/;
	value = value.replace(re9,'');
	const re10 = /!\[(.*?)\)/;
	return value.replace(re10,'');
}