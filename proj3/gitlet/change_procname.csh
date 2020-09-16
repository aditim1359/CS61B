#!/bin/csh -f

foreach file (*.java)

   echo "Changing $file ..."
   sed "s/get_map/getMap/g"               $file > $file.1
   sed "s/set_map/setMap/g"               $file.1 > $file.2
   sed "s/get_timestamp/getTimestamp/g"   $file.2 > $file.3
   sed "s/get_logMessage/getLogMessage/g"   $file.3 > $file.4
   sed "s/get_commitSHA1/getCommitSHA1/g"   $file.4 > $file.6
   sed "s/get_parent1/getParent1/g"   $file.6 > $file.7
   sed "s/get_parent2/getParent2/g"   $file.7 > $file.8

   #   mv $file.8 $file
   #   \rm -f $file.1 $file.2 $file.3 $file.4 $file.6 $file.7
end
