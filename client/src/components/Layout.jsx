import { Outlet, NavLink } from 'react-router-dom';
import styled from 'styled-components';

const Nav = styled.nav`
    display: flex;
    gap: var(--space-md);
    padding: var(--space-md) var(--space-lg);
    background: var(--color-surface);
    border-bottom: 1px solid var(--color-border);
`;

const StyledNavLink = styled(NavLink)`
    color: var(--color-text-muted);
    text-decoration: none;
    font-familiy: var(--font-body);

    &:hover {
        color: var(--color-text);
    }

    &:active {
        color: var(--color-accent);
    }
`;

export default function Layout() {
    return (
        <div>
            <Nav>
                <StyledNavLink to="/">Subscriptions</StyledNavLink>
                <StyledNavLink to="/about">About</StyledNavLink>
            </Nav>
            <Outlet />
        </div>
    )
}