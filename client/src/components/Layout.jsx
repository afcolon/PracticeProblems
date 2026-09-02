import { Outlet, NavLink } from 'react-router-dom';

export default function Layout() {
    return (
        <div>
            <nav>
                <NavLink to="/">Subscriptions</NavLink>
                <NavLink to="/about">About</NavLink>
            </nav>
            <Outlet />
        </div>
    )
}