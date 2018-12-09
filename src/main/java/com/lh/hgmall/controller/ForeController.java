package com.lh.hgmall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lh.hgmall.bean.*;
import com.lh.hgmall.dao.AdvertisementDAO;
import com.lh.hgmall.dao.CarouselDAO;
import com.lh.hgmall.dao.CategoryDAO;
import com.lh.hgmall.dao.OrderDAO;
import com.lh.hgmall.service.*;
import com.lh.hgmall.util.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;


@RestController
public class ForeController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    PropertyService propertyService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    CarouselService carouselService;
    @Autowired
    AdvertisementService advertisementService;
    @Autowired
    MessageService messageService;
    @Autowired
    PowerService powerService;
    @Autowired
    MemberService memberService;
    @Autowired
    StoreService storeService;
    @Autowired
    UserService userService;
    @Autowired
    ManagerService managerService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    OrderService orderService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    ExchangeService exchangeService;
    @Autowired
    MailService mailService;
    @Autowired
    LogisticsService logisticsService;
    @Autowired
    LogService logService;

    @GetMapping(value = "/foreHome")
    public Map<String, Object> home(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Category> categories = (List<Category>) request.getServletContext().getAttribute("categories");
        categoryService.fillRow(categories);
        List<Carousel> carousels = carouselService.listByType(CarouselDAO.type_home);
        map.put("categories", categories);
        map.put("carousels", carousels);
        return map;
    }

    @GetMapping(value = "/foreStore/{id}")
    public Store store(@PathVariable("id") int sid) throws Exception {
        Store store = storeService.get(sid);
        storeService.fill(store);
        return store;
    }

    @GetMapping(value = "/foreSearch/{key}")
    public Map<String, Object> search(@PathVariable("key") String key, @RequestParam(value = "orderItem") String order, @RequestParam(value = "sort") Boolean sort) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Category> categories = categoryService.listByTypeAndSid(CategoryDAO.type_product, 0);
        List<Product> hot = productService.listBySale(0).subList(0, 3);
        productService.fillFirst(hot);
        List<Advertisement> advertisements = advertisementService.listByType(AdvertisementDAO.type_category);
        int ran = (int) Math.random() * (advertisements.size() - 1);
        Advertisement advertisement = advertisements.get(ran);
        List<Product> products = productService.listByKeyAndStatus(0,key);
        products = SortUtil.sort(products, order);
        if (!sort)
            Collections.reverse(products);
        productService.fillFirst(products);
        productService.fillCategoryAndStore(products);
        map.put("categories", categories);
        map.put("hot", hot);
        map.put("advertisement", advertisement);
        map.put("products", products);
        return map;
    }


    @GetMapping(value = "/foreCategory/{cid}")
    public Map<String, Object> category(@PathVariable("cid") int cid, @RequestParam(value = "orderItem") String order, @RequestParam(value = "sort") Boolean sort) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Category> categories = categoryService.listByTypeAndSid(CategoryDAO.type_product, 0);
        List<Product> hot = productService.listBySale(0).subList(0, 3);
        productService.fillFirst(hot);
        List<Advertisement> advertisements = advertisementService.listByType(AdvertisementDAO.type_category);
        int ran = (int) Math.random() * (advertisements.size() - 1);
        Advertisement advertisement = advertisements.get(ran);
        Category category = categoryService.get(cid);
        categoryService.fillProduct(category);
        List<Product> products = category.getProducts();
        products = SortUtil.sort(products, order);
        if (!sort)
            Collections.reverse(products);
        productService.fillFirst(products);
        category.setProducts(products);
        categoryService.fillImg(category);
        map.put("categories", categories);
        map.put("category", category);
        map.put("hot", hot);
        map.put("advertisement", advertisement);
        return map;
    }

    @GetMapping(value = "/foreProduct/{pid}")
    public Map<String, Object> product(@PathVariable("pid") int pid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "4") int size) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Product product = productService.get(pid);
        productService.fillImage(product);
        productService.fillFirst(product);
        productService.fillCategoryAndStore(product);
        productService.fillReview(product);
        List<Property> properties = propertyService.listByCid(product.getCid());
        List<PropertyValue> propertyValues = propertyValueService.listByProduct(product.getId());
        PageUtil<Product> page = productService.listByCidAndStatus(start, size, 1, product.getCid(), 0);
        List<Product> products = page.getContent();
        products = new ArrayList<>(products);
        products.remove(product);
        productService.fillFirst(products);
        page.setContent(products);
        System.out.println(page.getContent());
        map.put("product", product);
        map.put("properties", properties);
        map.put("propertyValues", propertyValues);
        map.put("page", page);
        return map;
    }

    @GetMapping(value = "/foreHelp")
    public Map<String, Object> help() throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Category> categories = categoryService.listByTypeAndSid(CategoryDAO.type_article, 0);
        categoryService.fillArticle(categories);
        map.put("categories", categories);
        return map;
    }

    @PostMapping(value = "/foreMessage")
    public String addMessage(@RequestBody Message message, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        message.setCreateDate(new Date());
        message.setUid(user.getId());
        messageService.add(message);
        return "ok";
    }

    @PostMapping(value = "/foreView")
    public String addView(@RequestBody Message message, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        message.setCreateDate(new Date());
        message.setUid(user.getId());
        messageService.add(message);
        return "ok";
    }

    @GetMapping(value = "/foreMember")
    public Map<String, Object> member(HttpSession session) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Carousel> carousels = carouselService.listByType(CarouselDAO.type_member);
        List<Power> powers = powerService.listByMemberNot(1);
        List<Power> powerList = powerService.listByMember(1);
        Collections.reverse(powerList);
        map.put("carousels", carousels);
        map.put("powers", powers);
        map.put("normal", powerList);
        return map;
    }

    @GetMapping(value = "/forePower/{pid}")
    public Map<String, Object> power(@PathVariable("pid") int id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Power power = powerService.get(id);
        Member member = memberService.get(power.getMid());
        map.put("power", power);
        map.put("member", member);
        return map;
    }

    @PostMapping(value = "/forePower/{pid}")
    public String exchangePower(@PathVariable("pid") int id,HttpSession session) throws Exception {
        Power power = powerService.get(id);
        User user = (User) session.getAttribute("user");
        if(user.getScore()>=power.getScore()&&user.getMid()>=power.getMid())
        {
            user.setScore(user.getScore()-power.getScore());
            userService.update(user);
            session.setAttribute("user",user);
            return "ok";
        }
        return "no";

    }
    @GetMapping(value = "/foreLoginImage")
    public Map<String, Object> loginImage() throws Exception {
        List<Advertisement> advertisements1 = advertisementService.listByType(AdvertisementDAO.type_login);
        List<Advertisement> advertisements2 = advertisementService.listByType(AdvertisementDAO.type_loginStore);
        Map<String, Object> map = new HashMap<>();
        map.put("login", advertisements1.get(0));
        map.put("loginStore", advertisements2.get(0));
        return map;
    }


    @GetMapping(value = "/foreLoginCheck")
    public String loginCheck(HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "fail";
        return "success";
    }

    @GetMapping(value = "/foreAccountCheck")
    public String accountCheck(HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        if (user.getOpenDate() == null)
            return "fail";
        return "success";
    }

    @PostMapping(value = "/foreLoginUser")
    public String loginUser(@RequestBody User user, HttpServletRequest request) throws Exception {

        String name = user.getName();
        User trueUser = userService.get(name);
        if (trueUser == null)
            return "user404";
        else {
            String truePass = trueUser.getPassword();
            String pass = user.getPassword();
            pass = EncodeUtil.recode(pass, trueUser.getSalt());
            if (!truePass.equals(pass))
                return "fail";
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", trueUser);
        return "success";
    }

    @PostMapping(value = "/foreLoginStore")
    public String loginStore(@RequestBody Manager manager, HttpServletRequest request) throws Exception {

        String name = manager.getName();
        List<Manager> managers = managerService.listByName(name);
        if (managers.size() == 0)
            return "manager404";
        String pass = manager.getPassword();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, pass);
        try {
            subject.login(token);
            Manager manager1 = managerService.getByName(subject.getPrincipal().toString());
            Log log = new Log();
            log.setUid(manager1.getId());
            log.setCreateDate(new Date());
            log.setText("登录后台管理系统");
            logService.add(log);
            managerService.fill(manager1);
            HttpSession session = request.getSession();
            session.setAttribute("manager", manager1);
            return "success";
        } catch (AuthenticationException e) {
            return "fail";
        }
    }

    @GetMapping(value = "/foreApplyStore")
    public Map<String, Object> initApplyStore() throws Exception {
        List<Category> categories = categoryService.listByTypeAndSid(CategoryDAO.type_product, 0);
        Map<String, Object> map = new HashMap<>();
        map.put("categories", categories);
        return map;
    }

    @PostMapping(value = "/foreApplyStore")
    public void applyStore(@RequestBody Store store) throws Exception {
        store.setApplyDate(new Date());
        store.setStatus(2);
//        System.out.println(store);
        storeService.add(store);
    }

    @PostMapping(value = "/foreRegister")
    public void register(@RequestBody User user, HttpServletRequest request) throws Exception {
        Map<String, Object> map = EncodeUtil.encode(user.getPassword());
        user.setPassword(map.get("pass").toString());
        user.setSalt(map.get("salt").toString());
        user.setRegisterDate(new Date());
        userService.add(user);
        int id = user.getId();
        String oldFolder = "image/profile_user/default.jpg";
        String newFolder = "image/profile_user/";
        File image1 = new File(request.getServletContext().getRealPath(oldFolder));
        File image2 = new File(request.getServletContext().getRealPath(newFolder), id + ".jpg");
        FileCopyUtils.copy(image1, image2);
    }

    @PostMapping(value = "/foreBuyProduct")
    public String buyProduct(@RequestBody JSONObject object, HttpSession session) throws Exception {
        int pid = Integer.parseInt(object.get("pid").toString());
        int number = Integer.parseInt(object.get("number").toString());
        int sid = Integer.parseInt(object.get("sid").toString());
        OrderItem orderItem = new OrderItem();
        orderItem.setPid(pid);
        orderItem.setNumber(number);
        orderItem.setSid(sid);
        orderItem.setReview(0);
        User user = (User) session.getAttribute("user");
        orderItem.setUid(user.getId());
        orderItemService.add(orderItem);
        return "" + orderItem.getId();
    }

    @PostMapping(value = "/foreAddCart")
    public String AddCart(@RequestBody JSONObject object, HttpSession session) throws Exception {
        int cart = Integer.parseInt(session.getAttribute("shoppingCart").toString());
        if (cart >= 10)
            return "max";
        int pid = Integer.parseInt(object.get("pid").toString());
        int sid = Integer.parseInt(object.get("sid").toString());
        int number = Integer.parseInt(object.get("number").toString());
        OrderItem orderItem = new OrderItem();
        orderItem.setPid(pid);
        orderItem.setSid(sid);
        orderItem.setNumber(number);
        orderItem.setReview(0);
        User user = (User) session.getAttribute("user");
        orderItem.setUid(user.getId());
        orderItemService.add(orderItem);
        return "success";
    }

    @GetMapping(value = "/foreOrderItem/{ids}")
    public List<Store> orderItem(@PathVariable("ids") List<String> ids) throws Exception {
        List<OrderItem> orderItems = orderItemService.listByIds(ids);
        if (orderItems.size() == 0)
            return null;
        orderItemService.fill(orderItems);
        List<Store> stores = storeService.showOrderItem(orderItems);
        return stores;
    }

    @GetMapping(value = "/foreOrderItem")
    public List<Store> orderItems(HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUserAndOrder(user.getId(), 0);
        if (orderItems.size() == 0)
            return null;
        orderItemService.fill(orderItems);
        List<Store> stores = storeService.showOrderItem(orderItems);
        return stores;
    }

    @PutMapping(value = "/foreOrderItem/{id}")
    public OrderItem updateOrderItem(@RequestBody JSONObject object, @PathVariable("id") int id) throws Exception {
        int number = Integer.parseInt(object.get("number").toString());
        OrderItem orderItem = orderItemService.get(id);
        orderItem.setNumber(number);
        orderItemService.update(orderItem);
        orderItemService.fill(orderItem);
        return orderItem;
    }

    @DeleteMapping(value = "/foreOrderItem/{id}")
    public String deleteOrderItem(@PathVariable("id") int id) throws Exception {
        orderItemService.delete(id);
        return null;
    }

    @GetMapping(value = "/checkUser")
    public Map<String, Object> checkUser(HttpSession session) throws Exception {
        Map<String, Object> map = new HashMap<>();
        User user = (User) session.getAttribute("user");
        float sum = Float.parseFloat(session.getAttribute("sum").toString());
        if (user.getMid() != 0)
            map.put("result1", "yes");
        else
            map.put("result1", "no");
        if (user.getAccount() >= sum)
            map.put("result2", "yes");
        else
            map.put("result2", "no");
        return map;
    }

    @PostMapping(value = "/foreOrder")
    public String createOrder(@RequestBody JSONObject object, HttpSession session) throws Exception {
        List<Order> orders = JSON.parseArray(JSONArray.toJSONString(object.get("orders")), Order.class);
        JSONArray array2 = JSONObject.parseArray(JSONArray.toJSONString(object.get("ids")));
        List ids = array2.toJavaList(List.class);
        int i = 0;
        User user = (User) session.getAttribute("user");
        user.setScore(user.getScore() + ids.size());
        userService.update(user);
        String s = "wait";
        float sum = 0;
        int num = 0;
        String oids = "";
        for (Order order :
                orders) {
            String code = CalendarRandomUtil.getRandom();
            order.setOrderCode(code);
            order.setCreateDate(new Date());
            order.setUid(user.getId());
            if (order.getPayment().equals(OrderDAO.payment_online)) {
                order.setStatus(OrderDAO.type_waitPay);
                sum += order.getSum();
                num++;
                s = "pay";
                orderService.add(order);
                oids += "&oid=" + order.getId();
            } else {
                order.setStatus(OrderDAO.type_waitDelivery);
                orderService.add(order);
            }
            List<Integer> strings = (List<Integer>) ids.get(i);
            for (int j = 0; j < strings.size(); j++) {
                int id = strings.get(j);
                OrderItem item = orderItemService.get(id);
                item.setOid(order.getId());
                orderItemService.update(item);
                Product product = productService.get(item.getPid());
                product.setSales(item.getNumber());
                productService.update(product);
            }
            i++;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", s);
        jsonObject.put("sum", sum);
        jsonObject.put("num", num);
        jsonObject.put("ids", oids);
        session.setAttribute("sum", sum);
        return jsonObject.toJSONString();
    }

    @GetMapping(value = "/foreOrderList")
    public List<Order> orderList(HttpSession session, @RequestParam("type") String type) throws Exception {
        User user = (User) session.getAttribute("user");
        List<Order> orders;
        if (type.equals("all"))
            orders = orderService.listByUserAndStatusNotAndType(user.getId(), OrderDAO.type_delete, '0');
        else if (type.equals("exchange"))
            orders = orderService.listByUserAndStatusLike(user.getId(),OrderDAO.type_waitExchange, OrderDAO.type_waitRefund, OrderDAO.type_waitRejected);
        else
            orders = orderService.listByUserAndStatusAndType(user.getId(), type, '0');
        if (orders.size() == 0)
            return null;
        else {
            orderService.fillStore(orders);
            orderService.fillOrderItem(orders);
            return orders;
        }
    }

    @GetMapping(value = "/foreLogistics/{id}")
    public List<Logistics> logistics(@PathVariable("id") int oid) {
        List<Logistics> logistics = logisticsService.listByOrder(oid);
        return logistics;
    }

    @DeleteMapping(value = "/foreOrder/{id}")
    public String deleteOrder(@PathVariable("id") int id) throws Exception {
        Order order = orderService.get(id);
        order.setStatus(OrderDAO.type_delete);
        orderService.update(order);
        return null;
    }

    @PostMapping(value = "/forePayOrder")
    public String payOrder(HttpSession session, @RequestBody JSONObject object) throws Exception {
        String pass = object.get("password").toString();
        int style = Integer.parseInt(object.get("style").toString());
        List<String> oids = JSON.parseArray(object.get("oids").toString(), String.class);
        User user = (User) session.getAttribute("user");
        float sum = Float.parseFloat(session.getAttribute("sum").toString());
        String truePass = user.getPassword();
        String trueSalt = user.getSalt();
        String myPass = EncodeUtil.recode(pass, trueSalt);
        if (myPass.equals(truePass)) {
            if (style == 1) {
                if (user.getMid() == 0)
                    return "notMember";
                else {
                    if (user.getAccount() < sum - sum * 0.09)
                        return "lessAccount";
                    else {
                        float temp = (float) (user.getAccount() - (sum - sum * 0.09));
                        user.setAccount(temp);
                        userService.update(user);
                        session.setAttribute("user", user);
                    }
                }
            }
            if (style == 2)
                if (user.getAccount() < sum)
                    return "lessAccount";
                else {
                    user.setAccount(user.getAccount() - (sum));
                    userService.update(user);
                    session.setAttribute("user", user);
                }
            for (String s :
                    oids) {
                int oid = Integer.parseInt(s);
                Order order = orderService.get(oid);
                order.setPayDate(new Date());
                if (order.getPayment().equals(OrderDAO.payment_online))
                    order.setStatus(OrderDAO.type_waitDelivery);
                else if (order.getPayment().equals(OrderDAO.payment_delivery))
                    order.setStatus(OrderDAO.type_waitConfirm);
                else
                    order.setStatus(OrderDAO.type_waitReview);
                orderService.update(order);
            }
            return "yes";
        } else
            return "no";
    }

    @GetMapping(value = "/forePayOrder/{id}")
    public String payOrderPage(@PathVariable("id") int id, HttpSession session) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Order order = orderService.get(id);
        jsonObject.put("sum", order.getSum());
        jsonObject.put("num", 1);
        jsonObject.put("id", "&oid=" + id);
        session.setAttribute("sum", order.getSum());
        return jsonObject.toJSONString();
    }

    @GetMapping(value = "/foreConfirmOrder/{id}")
    public Order confirmOrderPage(@PathVariable("id") int id, HttpSession session) throws Exception {

        Order order = orderService.get(id);
        orderService.fillOrderItem(order);
        orderService.fillStore(order);
        return order;
    }

    @PostMapping(value = "/foreConfirmOrder")
    public String confirmOrder(HttpSession session, @RequestBody JSONObject object) throws Exception {
        String pass = object.get("password").toString();
        int oid = Integer.parseInt(object.get("oid").toString());
        User user = (User) session.getAttribute("user");
        String truePass = user.getPassword();
        String trueSalt = user.getSalt();
        String myPass = EncodeUtil.recode(pass, trueSalt);
        if (myPass.equals(truePass)) {
            Order order = orderService.get(oid);
            order.setConfirmDate(new Date());
            if (order.getPayment().equals(OrderDAO.payment_receiving))
                order.setStatus(OrderDAO.type_waitPay);
            else
                order.setStatus(OrderDAO.type_waitReview);
            orderService.update(order);
            return "yes";
        } else
            return "no";
    }

    @GetMapping(value = "/foreReview/{id}")
    public OrderItem reviewPage(@PathVariable("id") int id) throws Exception {
        OrderItem item = orderItemService.get(id);
        orderItemService.fill(item);
        orderItemService.fillOrder(item);
        return item;
    }

    @PostMapping(value = "/foreReview/{id}")
    public String review(HttpSession session, @RequestBody Review review, @PathVariable("id") int oid) throws Exception {
        User user = (User) session.getAttribute("user");
        review.setUid(user.getId());
        review.setCreateDate(new Date());
        reviewService.add(review);
        Product product = productService.get(review.getPid());
        product.setReviews(product.getReviews() + 1);
        productService.update(product);
        OrderItem item = orderItemService.get(oid);
        item.setReview(1);
        Order order = orderService.get(item.getOid());
        orderService.fillOrderItem(order);
        boolean found = false;
        for (OrderItem item1 :
                order.getOrderItems()) {
            if (item1.getReview() == 0) {
                found = true;
                break;
            }
        }
        if (!found) {
            order.setStatus(OrderDAO.type_success);
            orderService.update(order);
        }
        return "ok";
    }

    @GetMapping(value = "/foreReviewSuccess/{id}")
    public Map<String, Object> reviewSuccessPage(@PathVariable("id") int id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        OrderItem item = orderItemService.get(id);
        orderItemService.fill(item);
        orderItemService.fillOrder(item);
        map.put("item", item);
        List<Review> reviews = reviewService.listByProduct(item.getPid());
        reviewService.fill(reviews);
        map.put("reviews", reviews);
        return map;
    }

    @PostMapping(value = "/foreExchangeOrder/{id}")
    public String exchangeOrder(@PathVariable("id") int oid, @RequestBody JSONObject object) throws Exception {
        String status = object.get("status").toString();
        String describe = object.get("describe").toString();
        Order order = orderService.get(oid);
        order.setStatus(status);
        orderService.update(order);
        Exchange exchange = new Exchange();
        exchange.setOid(oid);
        exchange.setDescribe(describe);
        exchangeService.add(exchange);
        return "ok";
    }

    @GetMapping(value = "/foreUser")
    public User user(HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        return user;
    }

    @PutMapping(value = "/foreUser/{id}")
    public String modifyUser(HttpSession session, @RequestBody User user) throws Exception {
        userService.update(user);
        session.setAttribute("user", userService.get(user.getId()));
        return "ok";
    }

    @PutMapping(value = "/foreUser")
    public String changeUser(HttpSession session, @RequestBody JSONObject object) throws Exception {
        User user = (User) session.getAttribute("user");
        String type = object.get("type").toString();
        String key = object.get("key").toString();
        if (type.equals("mobile"))
            user.setMobile(key);
        else if (type.equals("email"))
            user.setEmail(key);
        else if (type.equals("password")) {
            Map<String, Object> map = EncodeUtil.encode(key);
            user.setSalt(map.get("salt").toString());
            user.setPassword(map.get("pass").toString());
        } else
            return "no";
        userService.update(user);
        session.setAttribute("user", userService.get(user.getId()));
        return "ok";
    }

    @PostMapping(value = "/foreUser")
    public String uploadUser(HttpSession session, HttpServletRequest request, MultipartFile image) throws Exception {
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        File imageFolder = new File(request.getServletContext().getRealPath("image/profile_user"));
        File file = new File(imageFolder, id + ".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
        return "ok";
    }

    @GetMapping(value = "/foreOpenAccount")
    public Map<String, Object> openAccountPage(HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        String random = RandomStringUtils.randomAlphanumeric(8);
        String from = "953625619@qq.com";
        String to = user.getEmail();
        String subject = "零号商城：您正在开通账户！";
        String content = "<html>\n" +
                "<body>\n" +
                "<BR>\n" +
                "<div align='center'>\n" +
                " <h3>恭喜您，开通成功！</h3>\n" +
                "    <h3>您的验证码为：<b>\"" + random + "\"</b></h3>" +
                "<BR>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendHtmlMail(from, to, subject, content);
        Map<String, Object> map = new HashMap<>();
        map.put("result", "ok");
        map.put("random", random);
        return map;
    }

    @PostMapping(value = "/foreOpenAccount")
    public String openAccount(HttpSession session, @RequestBody JSONObject object) throws Exception {
        String random = object.get("random").toString();
        String key = object.get("key").toString();
        if (key.equals(random)) {
            User user = (User) session.getAttribute("user");
            user.setOpenDate(new Date());
            user.setScore(user.getScore()+30);
            userService.update(user);
            session.setAttribute("user", userService.get(user.getId()));
            return "ok";
        }
        return "no";
    }

    @PostMapping(value = "/foreRecharge")
    public String recharge(HttpSession session, @RequestBody JSONObject object) {
        String pass = object.get("password").toString();
        User user = (User) session.getAttribute("user");
        float money = Float.parseFloat(object.get("money").toString());
        String truePass = user.getPassword();
        String trueSalt = user.getSalt();
        String myPass = EncodeUtil.recode(pass, trueSalt);
        if (myPass.equals(truePass)) {
            user.setAccount(user.getAccount() + money);
            user.setScore(user.getScore()+2);
            userService.update(user);
            orderService.recharge(money, user);
            return "ok";
        }
        return "no";
    }

    @GetMapping(value = "/forgetUser")
    public Map<String, Object> forgetUser(@RequestParam("email") String email){
        String result = "";
        String random = "";
        User user = userService.getByEmail(email);
        if(user==null)
            result =  "no";
        else
        {
            random = RandomStringUtils.randomAlphanumeric(8);
            String from = "953625619@qq.com";
            String to = user.getEmail();
            String subject = "零号商城：你正在找回你的密码！";
            String content = "<html>\n" +
                    "<body>\n" +
                    "<BR>\n" +
                    "<div align='center'>\n" +
                    " <h3>恭喜您，邮箱验证成功！</h3>\n" +
                    "    <h3>您的验证码为：<b>\"" + random + "\"</b></h3>" +
                    "<BR>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";
            mailService.sendHtmlMail(from, to, subject, content);
            result = "yes";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("result", result);
        map.put("random", random);
        map.put("email", email);
        return map;
    }

    @PostMapping(value = "/foreUserPass")
    public String setUserPass(@RequestBody JSONObject object){
        String random = object.get("random").toString();
        String key = object.get("key").toString();
        String pass = object.get("pass").toString();
        String email = object.get("email").toString();
        if (key.equals(random)) {
            User user = userService.getByEmail(email);
            Map<String, Object> map = EncodeUtil.encode(pass);
            user.setSalt(map.get("salt").toString());
            user.setPassword(map.get("pass").toString());
            userService.update(user);
            return "ok";
        }
        return "no";
    }

    @GetMapping(value = "/forgetManager")
    public Map<String, Object> forgetManager(@RequestParam("email") String email){
        String result = "";
        String random = "";
        Manager manager = managerService.getByEmail(email);
        if(manager==null)
            result =  "no";
        else
        {
            random = RandomStringUtils.randomAlphanumeric(8);
            String from = "953625619@qq.com";
            String to = manager.getEmail();
            String subject = "零号商城：你正在找回你的密码！";
            String content = "<html>\n" +
                    "<body>\n" +
                    "<BR>\n" +
                    "<div align='center'>\n" +
                    " <h3>恭喜您，邮箱验证成功！</h3>\n" +
                    "    <h3>您的验证码为：<b>\"" + random + "\"</b></h3>" +
                    "<BR>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";
            mailService.sendHtmlMail(from, to, subject, content);
            result = "yes";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("result", result);
        map.put("random", random);
        map.put("email", email);
        return map;
    }

    @PostMapping(value = "/foreManagerPass")
    public String setManagerPass(@RequestBody JSONObject object){
        String random = object.get("random").toString();
        String key = object.get("key").toString();
        String pass = object.get("pass").toString();
        String email = object.get("email").toString();
        if (key.equals(random)) {
            Manager manager = managerService.getByEmail(email);
            Map<String, Object> map = EncodeUtil.encode(pass);
            manager.setSalt(map.get("salt").toString());
            manager.setPassword(map.get("pass").toString());
            managerService.update(manager);
            return "ok";
        }
        return "no";
    }
}
