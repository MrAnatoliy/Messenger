import {Controller, FormProvider, useForm} from "react-hook-form";
import {Button} from "../components/ui/button.tsx";
import {Input} from "../components/ui/input.tsx";
import axios from "axios";

export const Login = () => {

    const methods = useForm()
    const {control, handleSubmit} = methods

    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    const onSave = async (data) => {
        axios.post('http://localhost:8080/api/auth/login', data, {
        })
            .then(response => {
                console.log(response.status)
                console.log(response.data)
            })
            .catch(error => {
                console.error(error)
            })
        console.log(data)
    }

    return (
        <FormProvider {...methods}>
            <div className="card">
                <span>Login</span>
                <Controller
                    control={control}
                    name="email"
                    render={({field : {value, onChange} }) => (
                    <Input type="email" placeholder="Email" value={value || ''} onChange={onChange}/>
                )}
                />

                <Controller
                    control={control}
                    name="password"
                    render={({field : {value, onChange} }) => (
                    <Input type="password" placeholder="Password" value={value || ''} onChange={onChange}/>
                )}
                />

                <Button onClick={handleSubmit(onSave)}>Submit</Button>

            </div>
        </FormProvider>
    );
}