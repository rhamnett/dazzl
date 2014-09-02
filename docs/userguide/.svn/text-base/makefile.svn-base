default: index.html

index.html: valid
	docbook2html index.xml

index.pdf: valid
	docbook2pdf index.xml

valid: index.xml
	xmllint --valid --noout index.xml
