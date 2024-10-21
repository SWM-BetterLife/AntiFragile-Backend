package swm.betterlife.antifragile.common.response;

public record ResponseBody<T>(Status status, String errorMessage, T data) {

    public static ResponseBody<Void> ok() {
        return new ResponseBody<>(Status.SUCCESS, null, null);
    }

    public static <T> ResponseBody<T> ok(T data) {
        return new ResponseBody<>(Status.SUCCESS, null, data);
    }

    public static ResponseBody<Void> fail(String errorMessage) {
        return new ResponseBody<>(Status.FAIL, errorMessage, null);
    }

    public static <T> ResponseBody<T> fail(String errorMessage, T data) {
        return new ResponseBody<>(Status.FAIL, errorMessage, data);
    }

    public static ResponseBody<Void> error(String errorMessage) {
        return new ResponseBody<>(Status.ERROR, errorMessage, null);
    }

    public static <T> ResponseBody<T> error(String errorMessage, T data) {
        return new ResponseBody<>(Status.ERROR, errorMessage, data);
    }
}
