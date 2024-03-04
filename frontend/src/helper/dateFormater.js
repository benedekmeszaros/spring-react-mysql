const format = (date) => {
  const d = new Date(date);
  return `${d.toDateString()} at ${d.getHours()}:${d.getMinutes()}`;
};

export default format;
