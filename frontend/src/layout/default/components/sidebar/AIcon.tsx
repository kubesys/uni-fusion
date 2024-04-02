import * as Icons from '@ant-design/icons-vue'

interface Module {
    [p:string]: any
}

function toCamelCase(str:string){
    return str
        .split("-")
        .map((e)=> e.charAt(0).toUpperCase() + e.slice(1))
        .join("")
}

export default {
    props: {
        icon:String
    },

    setup(props: {icon: string}) {
        const im: Module = Icons
        const tag = im[toCamelCase(props.icon)] //图标组件
        return ()=> <tag></tag>
    }
}
