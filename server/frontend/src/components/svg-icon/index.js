const requireAll = requireContext => requireContext.keys().map(requireContext)
const req = require.context('@/assets/svg', false, /\.svg$/)
requireAll(req)

export default {
    name: 'svg-icon',
    props: {
        iconClass: {
            type: String,
            required: true
        },
        className: {
            type: String,
            default: ''
        }
    },
    computed: {
        iconName() {
            return `#icon-${this.iconClass}`
        },
        svgClass() {
            if (this.className) {
                return 'svg-icon ' + this.className
            } else {
                return 'svg-icon'
            }
        }
    }
}