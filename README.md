Intern-Application-App
======================

App Flow:

- When the user first turns on the application, they are prompted to enter a search
- The search text is sent through an async task to fetch the JSON array, each object is parsed and is populated in a listview
- The user selects what they want, if their search was successful, by clicking on the view button found on each row
- The data from the row is passed to a new activity where it displays the Product name, Product brand, the price, the original price, the discount, and the thumbnail of the product.  Also, the product id is passed along as well but not visually.  There is a button where the user can click to be notified when their product is at a 20% or more discount the next time they load the application.
- Next time the user loads the application, a SQLite database holds the selected productId's that were selected from the product page.  They go through a similar process from above, but only those who have a discount of 20% or higher will be populated in the listview.  The user can then click on the row and turn off notifications if they wish.


I hope you enjoy the application, it was challenging with school, however very rewarding!  If you have any questions, do not hesitate to contact me!



