# Brewski
BreweryDb project

##Libraries and APIs used  
BreweryDb API - test application purposes only  
Retrofit 2  
Picasso - Image loading  
Android Support Library (Design, AppCompat, CardView)  


## Picasso Memory Issue
I had an issue with Picasso and loading images in the Recyclerview. If I ran through the full list, the memory usage would spike (and stay spiked) as they were kept in this view. I knew it was the images because it was fine when I removed them.
I was already using `.fit()` for the Adapter, but this didn't seem to help. Below was memory usage with `.fit()`
![Picasso Issue](https://github.com/DarkNormal/Brewski/blob/master/docs/memory-issue.PNG "Picasso Memory Issue")
You can see here that it reached ~50MB for a simple Recyclerview - not good.
I swapped it with `.resize(128,128)` which reduced the memory use significantly - here's the usage from the same action
![Picasso Issue](https://github.com/DarkNormal/Brewski/blob/master/docs/memory-issue-fix.png "Picasso Memory Issue")

It would be better if the BreweryDb provided better images for Breweries and labels - there's a lot of whitespace on them but that can't be changed.  
Some beers are also missing labels / Brewery information, so that's why the Query is so specific.

