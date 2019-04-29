
function set(key,value) {
    localStorage.setItem(key,JSON.stringify(value))
}
function get(key) {
    const value = localStorage.getItem(key)
    if(value)
    {
        return JSON.parse(value)
    }
    return value;
}

export default {
    set,
    get
}