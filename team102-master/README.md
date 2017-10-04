team102
=======

##Import in IntelliJ:
___________________

In Menubar auswähelen File -> Import Project .
Den Projektordner auswählen und in nächstem Fenster "Create project from existing source".
Nach der Bestimmung des Names und Ortes des neuen Projektordners muss ../src als Source ausgewählt werden.
Als Module sollte von IntelliJ automatisch erzeugte Module verwendet werden.
SDK Version muss 1.8 oder neuer sein.


##Sources Root ändern:
____________________

Falls ../src nicht als Source angezeigt ist muss es von Hand gemacht werden. 
Rechts-Klick auf src ordner und in "Mark Directory As..." Sources Root auswählen. 
Jetzt ist ../src Sources Root und blau angezeigt. Alle ander blaue Ordnern müssen in gleicher Weise durch "Unmark as Sources Root" geändert werden.

##JSON:
_____

Im Projektordner (nicht .../src sondern ../Projektfolder/src) muss gson-2.2.4.jar stehen. Das ist eine Bibliothek von JSON, die durch Rechtsklick -> Add as Library in IntelliJ als Bibliothek bestimmt werden muss.
Unten Link zum herunterladen:
https://code.google.com/p/google-gson/downloads/detail?name=google-gson-2.2.4-release.zip&can=2&q=

##Project language level (PLL):
_______________________

Version vom PLL muss midestens 7.0 sein. Oft als default auf 6.0 gestellt.
Falls dies dem Fall ist kann es durch Rechtsklick -> Open Module Settings unter Project geändert werden. 
Neuste version zur Zeit 9.0.
