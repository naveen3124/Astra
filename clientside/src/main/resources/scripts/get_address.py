#!/bin/python3

# Import modules for CGI handling 
import cgi, cgitb 
cgitb.enable()
# Create instance of FieldStorage 
form = cgi.FieldStorage() 
# Get data from fields

first_name = form.getvalue('loctype')
last_name  = form.getvalue('locname')

#print ("Content-type:text/html\r\n\r\n")
#print ("<html>")
#print ("<body>")
#print ("<h2>Hello %s %s</h2>" % (first_name, last_name))
#print ("</body>")
#print ("</html>")

print ("Content-type:text/html\r\n\r\n")

with open("park.html") as f:
	parkfile = f.read()
print(parkfile)
