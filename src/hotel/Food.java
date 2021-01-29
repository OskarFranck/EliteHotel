package hotel;

public class Food {

    final private MenuItem menuItem;

    public enum MenuItem {

        SANDWICH(150) {
            @Override
            public String toString() {
                return "Sandwich";
            }
        },
        PASTA(160) {
            @Override
            public String toString() {
                return "Pasta";
            }
        },
        NOODLES(170) {
            @Override
            public String toString() {
                return "Noodles";
            }
        },
        DRINK(30) {
            @Override
            public String toString() {
                return "Drink";
            }
        };

        private final int price;

        MenuItem(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }

    }

    Food(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public String toString() {
        return menuItem.toString();
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getPrice() {
        return menuItem.getPrice();
    }

}
