const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  info: state => state.user.info,
  hasLogin: state => state.user.info!=null
}
export default getters
