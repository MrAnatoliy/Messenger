import {Button} from "../components/ui/button.tsx";

export const MainPage = () => {
    return (
        <>
            <div className="h-screen flex justify-center items-center">
                <Button className="ms-10" onClick={
                    () => { window.location.href = "/login" }
                }>Login</Button>
                <Button className="ms-10" onClick={
                    () => { window.location.href = "/signup" }
                }>Register</Button>
            </div>
        </>

    );
};