const initDate = (oldDate, full) => {
  var odate = new Date(oldDate);
  var year = odate.getFullYear();
  var month =
    odate.getMonth() < 9 ? "0" + (odate.getMonth() + 1) : odate.getMonth() + 1;
  var date = odate.getDate() < 10 ? "0" + odate.getDate() : odate.getDate();
  if (full == "all") {
    var t = oldDate.split(" ")[0];
    return t.split("-")[1] + ", " + t.split("-")[2] + ", " + t.split("-")[0];
  } else if (full == "year") {
    return year;
  } else if (full == "month") {
    return odate.getMonth() + 1;
  } else if (full == "date") {
    return date;
  } else if (full == "newDate") {
    return month + ", " + date + ", " + year;
  }
};

export { initDate };
