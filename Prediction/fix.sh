for f in .data/*
do
  echo Fixing $f
  sort $f | uniq -u > temp
  mv temp $f
done
