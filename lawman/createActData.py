import re;
regex= r"(?P<act>[a-zA-Z\(\), ’`'-]+)\s+(?P<year>\d+)\s+(?P<actno>\d+)"
with open('actData.txt') as f:
    lines = f.readlines()
with open("centralActs.csv", "w") as f:
    f.write("Title,Year,ActNo"+ "\n")
    for line in lines:
        line = line.strip("\n").replace(",","").replace("'","")
        line = line.strip()
        matches= re.search(regex, line)

        act = matches.group('act')
        year = matches.group('year')
        actno = matches.group('actno')
        line = act + "[" + year + "]" + "," + year + "," + actno
        f.write(line + "\n")
#print(matches)
