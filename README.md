# balancer
Breaks a large folder of files into a set of folders roughly evenly

# Rationale

I had a huge folder of old folders that was slow to load in Finder and digikam. I put this together to break them apart. Internally it uses the two lead digits of the files md5 hash to pick a folder for it. It does not guarantee uniqueness, but should have a relatively even spread without hot spots. The uniform distribution of the hash should hold for the truncated versions.
