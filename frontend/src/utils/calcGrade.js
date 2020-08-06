export function getGrade(articleCount) {
  let grade = 0;
  if (articleCount === 0) {
    grade = 1;
  } else if (articleCount <= 99) {
    grade = 2;
  } else if (articleCount <= 199) {
    grade = 3;
  } else {
    grade = 4;
  }
  return grade;
}
