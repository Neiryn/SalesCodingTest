<h2>SalesCodingTest</h2>

URL to use (with default 8080 port) : GET localhost:8080/tax/order

Input examples in json format :
- Example 1 :
```
{
    "productList" : [
        {"product" : "book", "count" : 1, "price" : 12.49},
        {"product" : "music CD", "count" : 1, "price" : 14.99},
        {"product" : "chocolate bar", "count" : 1, "price" : 0.85}
    ]
}
```

- Example 2
```
{
    "productList" : [
        {"product" : "imported box of chocolates", "count" : 1, "price" : 10},
        {"product" : "imported bottle of perfume", "count" : 1, "price" : 47.5}
    ]
}
```

- Example 3
```
{
    "productList" : [
        {"product" : "imported bottle of perfume", "count" : 1, "price" : 27.99},
        {"product" : "bottle of perfume", "count" : 1, "price" : 18.99},
        {"product" : "packet of headache pills", "count" : 1, "price" : 9.75},
        {"product" : "imported box of chocolates", "count" : 1, "price" : 11.25}
    ]
}
```
 