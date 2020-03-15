package pro.sisit.javacourse.hint;

import pro.sisit.javacourse.Producer;
import pro.sisit.javacourse.Product;
import pro.sisit.javacourse.ProductType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для работы с продуктами
 * Дубликат сервиса с реализованными методами
 */
public class ProductsServiceHint {

    public static void main(String[] args) {
    }

    /**
     * Фильтрует переданный список продуктов по переданному типу список продуктов.
     */
    public static List<Product> filterByType(List<Product> products, ProductType type) {
        return products.stream()
                .filter(product -> product.getType() == type)
                .collect(Collectors.toList());
    }

    /**
     * Получает отсортированный список уникальных производителей переданных продуктов.
     */
    public static List<Producer> getUniqueProducers(List<Product> products) {
        return products.stream()
                .map(Product::getProducer)
                .distinct()
                .sorted(Comparator.comparing(Producer::getName))
                .collect(Collectors.toList());
    }

    /**
     * Рассчитвает долю НДС для списка продуктов.
     * Долю НДС для одного продукта считаем по формуле: цена * 20 / 120,
     * при этом при делении используется банковское округление.
     */
    public static BigDecimal getTotalTax(List<Product> products) {
        return products.stream()
                .peek(product -> System.out.print(product.getName()))
                .map(Product::getPrice)
                .map(price -> price.multiply(BigDecimal.valueOf(20.00)).divide(BigDecimal.valueOf(120.00), RoundingMode.HALF_EVEN))
                .peek(tax -> System.out.printf(", доля НДС: %s\n", tax))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Выполняет поиск продукта в переданном списке по переданному идентификатору продукта.
     * Если продукт найден, то вернет его, иначе вернет null.
     * Метод - в императивном стиле без использования стримов.
     */
    public static Product findProductByIdUnsafe(List<Product> products, long id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    /**
     * Определяет наименование производителя продукта по переданному идентификатору продукта
     */
    public static String detectProducerNameUnsafe(List<Product> products, long id) {
        Product product = ProductsServiceHint.findProductByIdUnsafe(products, id);
        return ProductsServiceHint.getProducerNameByProduct(product);
    }

    /**
     * Выполняет поиск продукта в переданном списке по переданному идентификатору продукта
     */
    public static Optional<Product> findProductById(List<Product> products, long id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst();
    }

    /**
     * Определяет наименование производителя продукта по переданному идентификатору продукта
     * Если продукт не найден вернет 'Неизвестный производитель'.
     */
    public static String detectProducerNameSoft(List<Product> products, long id) {
        return ProductsServiceHint.findProductById(products, id)
                .map(ProductsServiceHint::getProducerNameByProduct)
                .orElse("Неизвестный производитель");
    }

    /**
     * Получает продукт в переданном списке по переданному идентификатору продукта.
     * Если продукт найден, то вернет его, иначе выбросит RuntimeException с сообщением
     * Товар с идентификатором '%s' не найден
     */
    public static Product getProductById(List<Product> products, long id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Товар с идентификатором '%s' не найден", id)));
    }

    /**
     * Определяет наименование производителя продукта по переданному идентификатору продукта.
     * Если продукт не найден выбросит RuntimeException с сообщением
     * Товар с идентификатором '%s' не найден
     */
    public static String detectProducerName(List<Product> products, long id) {
        Product product = ProductsServiceHint.getProductById(products, id);
        return ProductsServiceHint.getProducerNameByProduct(product);
    }

    /**
     * Получает наименование производителя продукта по переданному продукту
     */
    public static String getProducerNameByProduct(Product product) {
        return product.getProducer().getName();
    }
}